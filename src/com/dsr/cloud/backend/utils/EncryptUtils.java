package com.dsr.cloud.backend.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class EncryptUtils {
	public static final String AES_PCBC_PKCS5="AES/PCBC/PKCS5Padding";
	public static final String AES_CBC_PKCS7="AES/CBC/PKCS7Padding";
	public static final String Charset="0123456789abcdef";
//	private static Codec base64Codec=new CodecBase64AImpl();
	private static final byte[] encodeTable;
	private static final int[] decodeTable;
	static{
		encodeTable=new byte[16];
		for(int i=0;i<16;i++){
			encodeTable[i]=(byte)Charset.charAt(i);
		}
		decodeTable=new int[256];
		for(int i=0;i<256;i++){
			int index=Charset.indexOf(i);
			if(index<0){
				index=Charset.toLowerCase().indexOf(i);
			}
			decodeTable[i]=index;
		}
	}
	static{
		try{
			@SuppressWarnings("unchecked")
			Class<Provider> cls=(Class<Provider>) Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider");
			Security.addProvider(cls.newInstance());
		}catch(Throwable e){//没有也没关系, 比如JAVA语言的客户端, 可以不需要该算法
			System.out.println(EncryptUtils.class.getName()+": no security provider for algorithm [AES_CBC_PKCS7], which is appropriate C#");
		}
	}
	private EncryptUtils(){}
	public static final String createHexDigest(byte[] input) throws Exception {
		return toHexString(digestMD5(input));
	}
	public static final String toHexString(byte[] input){
		byte[] obs=new byte[input.length<<1];
		for(int i=0;i<input.length;i++){
			obs[i<<1]=encodeTable[(0xF0&input[i])>>4];
			obs[(i<<1)|1]=encodeTable[0x0F&input[i]];
		}
		return new String(obs);
	}
	
	//摘要
	/**
	 * 使用SHA-512算法生成摘要
	 * @param input 二进制数据
	 * @return 二进制摘要
	 * @throws NoSuchAlgorithmException 
	 */
	public static final byte[] digest(byte[] input) throws NoSuchAlgorithmException{
		return MessageDigest.getInstance("SHA-512").digest(input);
	}
	/**
	 * 使用SHA-128算法生成摘要
	 * @param input 二进制数据
	 * @return 二进制摘要
	 * @throws NoSuchAlgorithmException 
	 */
	public static final byte[] digest128(byte[] input) throws NoSuchAlgorithmException{
		return MessageDigest.getInstance("SHA").digest(input);
	}
	/**
	 * 使用SHA-256算法生成摘要
	 * @param input 二进制数据
	 * @return 二进制摘要
	 * @throws NoSuchAlgorithmException 
	 */
	public static final byte[] digest256(byte[] input) throws NoSuchAlgorithmException{
		return MessageDigest.getInstance("SHA-256").digest(input);
	}
	/**
	 * 使用SHA-1算法生成摘要
	 * @param input 二进制数据
	 * @return 二进制摘要
	 * @throws NoSuchAlgorithmException 
	 */
	public static final byte[] digestSHA1(byte[] input) throws NoSuchAlgorithmException{
		return MessageDigest.getInstance("SHA-1").digest(input);
	}
	/**
	 * 使用MD5算法生成摘要
	 * @param input 二进制数据
	 * @return 二进制摘要
	 * @throws NoSuchAlgorithmException 
	 */
	public static final byte[] digestMD5(byte[] input) throws NoSuchAlgorithmException{
		return MessageDigest.getInstance("MD5").digest(input);
	}
	
	
	//加解密
	/**
	 * 使用指定算法加密
	 * @param algorithm 采用的加密算法
	 * @param keyt 密钥的二进制形式, 实际使用该参数的MD5摘要生成的16字节作为算法的密钥
	 * @param iv 向量参数的二进制形式, 实际使用该参数的MD5摘要生成的16字节作为算法的向量参数
	 * @param input 明文的二进制形式
	 * @return 密文的二进制形式
	 * @throws Exception
	 */
	public static final byte[] encrypt(String algorithm, byte[] keyt, byte[] iv, byte[] input) throws Exception {
		return doFinal(true, algorithm, keyt, iv, input);
	}
	/**
	 * 使用指定算法解密
	 * @param algorithm 采用的加密算法
	 * @param keyt 密钥的二进制形式, 实际使用该参数的MD5摘要生成的16字节作为算法的密钥
	 * @param iv 向量参数的二进制形式, 实际使用该参数的MD5摘要生成的16字节作为算法的向量参数
	 * @param input 密文的二进制形式
	 * @return 明文的二进制形式
	 * @throws Exception
	 */
	public static final byte[] decrypt(String algorithm, byte[] keyt, byte[] iv, byte[] input) throws Exception {
		return doFinal(false, algorithm, keyt, iv, input);
	}
	/**
	 * 使用指定算法加密
	 * @param algorithm 采用的加密算法
	 * @param keyt 密钥的二进制形式, 实际使用该参数的MD5摘要生成的16字节作为算法的密钥
	 * @param iv 向量参数的二进制形式, 实际使用该参数的MD5摘要生成的16字节作为算法的向量参数
	 * @param is 明文输入流
	 * @param off 需要解密的密文起始偏移量
	 * @param len 需要加密的明文片段长度
	 * @param os 密文输出流
	 * @throws Exception
	 */
	public static final void encrypt(String algorithm, byte[] keyt, byte[] iv, InputStream is, int len, OutputStream os) throws Exception {
		doFinal(true, algorithm, keyt, iv, is, len, os);
	}
	/**
	 * 使用指定算法解密
	 * @param algorithm 采用的加密算法
	 * @param keyt 密钥的二进制形式, 实际使用该参数的MD5摘要生成的16字节作为算法的密钥
	 * @param iv 向量参数的二进制形式, 实际使用该参数的MD5摘要生成的16字节作为算法的向量参数
	 * @param is 密文输入流
	 * @param off 需要解密的密文起始偏移量
	 * @param len 需要解密的密文片段长度
	 * @param os 密文输出流
	 * @throws Exception
	 */
	public static final void decrypt(String algorithm, byte[] keyt, byte[] iv, InputStream is, int len, OutputStream os) throws Exception {
		doFinal(false, algorithm, keyt, iv, is, len, os);
	}

	/**
	 * 使用指定算法加密, 参数密文均为字符串形式, 基本编码方式为UTF-8
	 * @param algorithm 采用的加密算法
	 * @param keyt 密钥的字符串形式, 实际使用该参数的二进制数据使用MD5摘要生成的16字节作为算法的密钥
	 * @param iv 向量参数的字符串形式, 实际使用该参数的二进制数据使用MD5摘要生成的16字节作为算法的向量参数
	 * @param input 明文的字符串形式, 实际针对该参数的UTF-8编码的二进制数据进行加密
	 * @return 返回二进制密文数据的BASE64编码字符串
	 * @throws Exception
	 */
	public static final String encrypt(String algorithm, String keyt, String iv, String input) throws Exception {
		byte[] cipherBytes=doFinal(true, algorithm, keyt.getBytes("UTF-8"), iv.getBytes("UTF-8"), input.getBytes("UTF-8"));
		return new String(Base64.encode(cipherBytes));
	}
	/**
	 * 使用指定算法解密, 参数明文均为字符串形式, 基本编码方式为UTF-8
	 * @param algorithm 采用的加密算法
	 * @param keyt 密钥的字符串形式, 实际使用该参数的二进制数据使用MD5摘要生成的16字节作为算法的密钥
	 * @param iv 向量参数的字符串形式, 实际使用该参数的二进制数据使用MD5摘要生成的16字节作为算法的向量参数
	 * @param input 二进制密文数据的BASE64编码字符串
	 * @return 明文的字符串形式, UTF-8编码
	 * @throws Exception
	 */
	public static final String decrypt(String algorithm, String keyt, String iv, String input) throws Exception {
		byte[] cipherBytes=Base64.decode(input.getBytes("UTF-8"));
		byte[] plaintBytes=doFinal(false, algorithm, keyt.getBytes("UTF-8"), iv.getBytes("UTF-8"), cipherBytes);
		return new String(plaintBytes, "UTF-8");
	}
	
	private static byte[] doFinal(boolean isEncrypt, String algorithm, byte[] keyt, byte[] iv, byte[] bytes) throws Exception{		
		Cipher cipher=getCipher(isEncrypt, algorithm, keyt, iv);
		return cipher.doFinal(bytes);
	}
	private static void doFinal(boolean isEncrypt, String algorithm, byte[] keyt, byte[] iv, InputStream is, int len, OutputStream os) throws Exception{
		if(is.available()<len)throw new IOException("unexcepted length");//剩余字节数不够
		if(!isEncrypt){
			if((len&0x0F)!=0)throw new IOException("unexcepted length of cipher bytes");
		}
		Cipher cipher=getCipher(isEncrypt, algorithm, keyt, iv);
		if(len>65536){//每次加解密65536个字节
			int loop=len/65536;//循环次数
			int modulus=len%65536;//剩余量			
			byte[] buffin=new byte[65536];
			byte[] buffout=new byte[65536];
			for(int i=0;i<loop;i++){
				is.read(buffin);
				int outlen=cipher.update(buffin, 0, 65536, buffout);
				os.write(buffout, 0, outlen);
			}
			if(modulus>0){
				is.read(buffin, 0, modulus);
				byte[] lastBlock=cipher.doFinal(buffin, 0, modulus);
				os.write(lastBlock);
			}
		}else{
			byte[] b=new byte[len];
			is.read(b);
			byte[] be=cipher.doFinal(b);
			os.write(be);
		}
	}
	private static final Cipher getCipher(boolean isEncrypt, String algorithm, byte[] keyt, byte[] iv) throws Exception{
		Cipher cipher=Cipher.getInstance(algorithm);
		int mode=Cipher.DECRYPT_MODE;
		if(isEncrypt){
			mode=Cipher.ENCRYPT_MODE;	
		}
		cipher.init(mode, new SecretKeySpec(digestMD5(keyt), "AES")
		, new IvParameterSpec(digestMD5(iv))
		);
		return cipher;
	}
	public static void main(String[] args) throws Exception{
		String pwd="123456789";
		String input = "简达测试123";
		System.out.println(EncryptUtils.toHexString(digestMD5(pwd.getBytes("UTF-8"))));
//		System.out.println(new String(digestMD5(pwd.getBytes("UTF-8")),"UTF-8"));
		
//		System.out.println(EncryptUtils.toHexString(EncryptUtils.encrypt("AES", pwd.getBytes("UTF-8"), pwd.getBytes("UTF-8"), input.getBytes("UTF-8"))));
	}
}
