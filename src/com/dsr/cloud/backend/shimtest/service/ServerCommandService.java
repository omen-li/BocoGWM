package com.dsr.cloud.backend.shimtest.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.dsr.cloud.backend.cache.CommandCache;
import com.dsr.cloud.backend.utils.MyLogger;
import com.dsr.cloud.backend.utils.StringUtil;
import com.dsr.cloud.backend.utils.Uuid32Utils;
import com.dsr.gateway.manager.IGatewayCommandService;
import com.dsr.gateway.manager.dto.api.gateway.CommandInfo;
import com.dsr.gateway.manager.dto.api.gateway.CommandRequest;
import com.dsr.gateway.manager.dto.api.gateway.CommandResponse;
import com.dsr.gateway.manager.dto.api.gateway.CommandResult;
import com.dsr.gateway.manager.dto.api.gateway.GeneralResponse;
import com.dsr.gateway.manager.dto.api.gateway.GeneralResponseResultCode;
import com.dsr.gateway.manager.exception.CommandErrorException;
import com.dsr.gateway.manager.exception.ExpiredDataException;
import com.dsr.gateway.manager.exception.ServerValidationException;
import com.dsr.gateway.manager.exception.UnknownDeviceException;
import com.dsr.gateway.manager.exception.UnknownGatewayException;

/**
 *
 * @author Artem.Sidorkin
 */
@Service(value = "gatewayCommandService")
public class ServerCommandService implements IGatewayCommandService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerCommandService.class);   
	
    private static Path commandFilePath;
    
//    private static final int LONG_POLLING_TIMES = 55;
	
    private static Hashtable<String, List<Request>> requestMap = new Hashtable<String, List<Request>>();
    
//	private static List<Request> requestList = new ArrayList<>();
	private static List<CommandInfo> commandsFromFile = new ArrayList<>();

    public ServerCommandService() throws URISyntaxException, MalformedURLException {
        URL jarLocation = ServerCommandService.class.getProtectionDomain().getCodeSource().getLocation();
        Path jarPath = Paths.get(jarLocation.toURI());
        commandFilePath = jarPath.getParent();
        LOGGER.info("Reading commands from: {}", commandFilePath.toString());
    }
	
	public Path getCommandFilePath() throws URISyntaxException {
		return commandFilePath;
	}

    @Override
    public void getServerCommand(
            final DeferredResult<CommandResponse> deferredResult,
            final HttpServletResponse servletResponse, 
            final CommandRequest request,
            final String ipAddress) 
                throws ServerValidationException, UnknownDeviceException, UnknownGatewayException {
    	
    	String gatewayId = request.getGatewayId();
    	gatewayId= Uuid32Utils.getId2Boco(gatewayId);
    	
    	
    	List<CommandInfo> commandInfoList = new ArrayList<CommandInfo>();
    	//检索command缓存,如果不存在或者已经消费完了则从数据库抽取
		try {
			processCommand(deferredResult, request, null,"");
		} catch (Exception e) {
			MyLogger.error("GW["+ gatewayId +"]消费command异常...",e);
			CommandResponse response = new CommandResponse();
			response.setResultCode(GeneralResponseResultCode.ERR);
			response.setCommand(null);
			deferredResult.setResult(response);
		}
    	
   
    }
    
    public void processCommand(final DeferredResult<CommandResponse> deferredResult, final CommandRequest commandRequest,
			final CommandInfo command,String gatewayId){
		if(commandRequest!=null ){
			if(StringUtil.isEmpty(gatewayId))
				gatewayId = commandRequest.getGatewayId();
			
			if(gatewayId.indexOf("-")<0)
				gatewayId= Uuid32Utils.getId2Boco(commandRequest.getGatewayId());
		}
		if (deferredResult != null) {
			if(CommandCache.exists(gatewayId)){
				CommandInfo commandToExecute =CommandCache.custCommand(gatewayId);
				MyLogger.info("GW[" + gatewayId + "] find the command list in cache, ready to custom...");
				deferredResult.setResult(generateServerToGatewayResponse(commandToExecute, commandRequest.getGatewayId()));
				Integer commandId = commandToExecute.getCommandId();
				MyLogger.info("GW[" + gatewayId + "] has successfully consumed command[" + commandToExecute.getName()+"]" );
			}else{
				MyLogger.info("GW[" + gatewayId + "] didn't find the command in cache, start waitCommand process...");
				waitCommandResponse(deferredResult, commandRequest);
				
			}
			
		} else if (command != null) {
			
			List<Request> requestList =  new ArrayList<>();
	    	if(requestMap.containsKey(gatewayId)){
	    		requestList = requestMap.get(gatewayId);
	    	}
			
			if (!requestList.isEmpty()) {
				Request request = requestList.get(0);
				MyLogger.info("GW["+ gatewayId +"] get the command during Request is alive, gatewayId in request:" + request.getCommandRequest().getGatewayId());
				request.getDeferredResult().setResult(generateServerToGatewayResponse(command, request.getCommandRequest().getGatewayId()));
				requestList.remove(0);
				requestMap.put(gatewayId, requestList);
			} else {
//				commandsFromFile.add(command);
				CommandCache.add(Uuid32Utils.getId2Gwm(command.getDeviceId()), command);
				MyLogger.info("GW["+ gatewayId +"] get the command during Request is not avlie,command:" +command.getName() + "will be set to the cache");
				
			}
		}
	}
    
    private void waitCommandResponse(final DeferredResult<CommandResponse> deferredResult, final CommandRequest commandRequest) {
    	String deviceId = Uuid32Utils.getId2Boco(commandRequest.getGatewayId());
    	
    	List<Request> requestList =  new ArrayList<>();
    	if(requestMap.containsKey(deviceId)){
    		requestList = requestMap.get(deviceId);
    	}
    	requestList.add(new Request(deferredResult, commandRequest));
    	requestMap.put(deviceId, requestList);
    	
        deferredResult.onTimeout(new Runnable() {
            @Override
            public void run() {
                synchronized (deferredResult) {
                	String deviceId = Uuid32Utils.getId2Boco(commandRequest.getGatewayId());
                    if (!deferredResult.isSetOrExpired()) {
                        deferredResult.setResult(generateServerToGatewayResponse(null, commandRequest.getGatewayId()));
                    }
                    
                    List<Request> requestList = requestMap.get(deviceId);
                    requestList.remove(0);
                    requestMap.put(deviceId, requestList);
                }
            }
        });
    }
    
    
    private CommandResponse generateServerToGatewayResponse(CommandInfo cmd, String gatewaySerialNumber) {
        CommandResponse response = new CommandResponse();
        if (cmd != null) {
            response.setResultCode(GeneralResponseResultCode.ACK);
            MyLogger.info("Command is found for GW: Gateway id = " +gatewaySerialNumber + ", command id = " + cmd.getCommandId() 
            		+ ", command name = " +cmd.getName() +", parameter name = " +cmd.getParameterName() + ", parameters list=" + cmd.getParameters());
            
            response.setCommand(cmd);
        } else {
        	MyLogger.debug("No commands present in the cloud side but gateway tries to get one.");
            response.setResultCode(GeneralResponseResultCode.ACK);
            response.setCommand(null);
        }
        return response;
    }

   

    /**
     * "Gateway" uses this call after server command processed to notify cloud about success or problems.
     *
     * @param commandResult
     * @return response DTO
     * @throws ServerValidationException
     * @throws ExpiredDataException
	 * @throws CommandErrorException
	 * @throws UnknownGatewayException
     */
    @Override
    public GeneralResponse postServerCommandResult(CommandResult commandResult) throws ServerValidationException, ExpiredDataException, CommandErrorException, UnknownGatewayException {
          LOGGER.info("postServerCommandResult: command result GW: '{}',  command Id: '{}', result: {}", 
                commandResult.getGatewayId(), commandResult.getCommandId(), commandResult.getResultCode());

        return new GeneralResponse(GeneralResponseResultCode.ACK);
    }
	
	public class Request {
		private DeferredResult<CommandResponse> deferredResult;
		private CommandRequest commandRequest;

		public Request(DeferredResult<CommandResponse> deferredResult, CommandRequest commandRequest){
			this.deferredResult = deferredResult;
			this.commandRequest = commandRequest;
		}
		
		public DeferredResult<CommandResponse> getDeferredResult() {
			return deferredResult;
		}

		public void setDeferredResult(DeferredResult<CommandResponse> deferredResult) {
			this.deferredResult = deferredResult;
		}

		public CommandRequest getCommandRequest() {
			return commandRequest;
		}

		public void setCommandRequest(CommandRequest commandRequest) {
			this.commandRequest = commandRequest;
		}
	}
}
