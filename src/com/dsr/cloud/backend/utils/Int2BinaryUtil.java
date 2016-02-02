package com.dsr.cloud.backend.utils;

import java.util.HashMap;
import java.util.Map;

public class Int2BinaryUtil {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int i = 1;  
        
        String a = getBinaryStr("i", i);  
        
        Map<Integer,Integer> valueMap =getValueMap(i);
        System.out.println(valueMap.size());
	}

	
	 public static String getBinaryStr(String s, int i)  
	    {  
	    	StringBuffer sb =  new StringBuffer();
	    	
	        for (int j = 31; j >= 0; j--)  
	        {  
	            if (((1 << j) & i) != 0)  
	            {  
	                sb.append("1");
	            }  
	            else  
	            {  
	                sb.append("0");
	            }  
	        }
	        return sb.toString();
	    }
	 
	 public static Map<Integer,Integer> getValueMap(int i){
		 
		 Map<Integer,Integer> valueMap =  new HashMap<Integer, Integer>();
		 
		 for (int j = 31; j >= 0; j--)  
		 {  
			 if (((1 << j) & i) != 0)  
			 {  
				 
				 valueMap.put(j, 1);
			 }  
			 else  
			 {  
			 }  
		 }
		 return valueMap;
		 
	 }
	 
	
}
