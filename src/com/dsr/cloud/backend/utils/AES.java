package com.dsr.cloud.backend.utils;


import java.io.UnsupportedEncodingException;  
import java.security.InvalidKeyException;  
import java.security.NoSuchAlgorithmException;  
import java.security.SecureRandom;  
  






import javax.crypto.BadPaddingException;  
import javax.crypto.Cipher;  
import javax.crypto.IllegalBlockSizeException;  
import javax.crypto.KeyGenerator;  
import javax.crypto.NoSuchPaddingException;  
import javax.crypto.SecretKey;  
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;  

import sun.misc.BASE64Encoder;  
  
public class AES {  
    /** 
      * 加密 
     *  
      * @param content 需要加密的内容 
     * @param password  加密密码 
     * @return 
      */   
     public static byte[] encrypt(String content, String password) {   
             try {              
                     KeyGenerator kgen = KeyGenerator.getInstance("AES");   
                     kgen.init(128, new SecureRandom(password.getBytes()));   
                     SecretKey secretKey = kgen.generateKey();   
                     byte[] enCodeFormat = secretKey.getEncoded();   
                     SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");   
                     Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
                     byte[] byteContent = content.getBytes("utf-8");   
                     cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
                     byte[] result = cipher.doFinal(byteContent);   
                     return result; // 加密   
             } catch (NoSuchAlgorithmException e) {   
                     e.printStackTrace();   
             } catch (NoSuchPaddingException e) {   
                     e.printStackTrace();   
             } catch (InvalidKeyException e) {   
                     e.printStackTrace();   
             } catch (UnsupportedEncodingException e) {   
                     e.printStackTrace();   
             } catch (IllegalBlockSizeException e) {   
                     e.printStackTrace();   
             } catch (BadPaddingException e) {   
                     e.printStackTrace();   
             }   
             return null;   
     }   
       
     /**解密 
      * @param content  待解密内容 
      * @param password 解密密钥 
      * @return 
       */   
      public static byte[] decrypt(byte[] content, String password) {   
              try {   
                       KeyGenerator kgen = KeyGenerator.getInstance("AES");   
                       kgen.init(128, new SecureRandom(password.getBytes()));   
                       SecretKey secretKey = kgen.generateKey();   
                       byte[] enCodeFormat = secretKey.getEncoded();   
                       SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");               
                       Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
                      cipher.init(Cipher.DECRYPT_MODE, key);// 初始化   
                      byte[] result = cipher.doFinal(content);   
                      return result; // 加密   
              } catch (NoSuchAlgorithmException e) {   
                      e.printStackTrace();   
              } catch (NoSuchPaddingException e) {   
                      e.printStackTrace();   
              } catch (InvalidKeyException e) {   
                      e.printStackTrace();   
              } catch (IllegalBlockSizeException e) {   
                      e.printStackTrace();   
              } catch (BadPaddingException e) {   
                      e.printStackTrace();   
              }   
              return null;   
      }  
        
        
      /**将二进制转换成16进制 
       * @param buf 
        * @return 
        */   
       public static String parseByte2HexStr(byte buf[]) {   
               StringBuffer sb = new StringBuffer();   
               for (int i = 0; i < buf.length; i++) {   
                       String hex = Integer.toHexString(buf[i] & 0xFF);   
                       if (hex.length() == 1) {   
                               hex = '0' + hex;   
                       }   
                       sb.append(hex.toUpperCase());   
               }   
               return sb.toString();   
       }   
         
       /**将16进制转换为二进制 
        * @param hexStr 
         * @return 
         */   
        public static byte[] parseHexStr2Byte(String hexStr) {   
                if (hexStr.length() < 1)   
                        return null;   
                byte[] result = new byte[hexStr.length()/2];   
                for (int i = 0;i< hexStr.length()/2; i++) {   
                        int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);   
                        int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);   
                        result[i] = (byte) (high * 16 + low);   
                }   
                return result;   
        }   
          
        /** 
           * 加密 
          * 
           * @param content 需要加密的内容 
          * @param password  加密密码 
          * @return 
           */   
          public static byte[] encrypt2(String content, String password) {   
                  try {   
                          SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");   
                          Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");   
                          byte[] byteContent = content.getBytes("utf-8");   
                          cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
                          byte[] result = cipher.doFinal(byteContent);   
                          return result; // 加密   
                  } catch (NoSuchAlgorithmException e) {   
                          e.printStackTrace();   
                  } catch (NoSuchPaddingException e) {   
                          e.printStackTrace();   
                  } catch (InvalidKeyException e) {   
                          e.printStackTrace();   
                  } catch (UnsupportedEncodingException e) {   
                          e.printStackTrace();   
                  } catch (IllegalBlockSizeException e) {   
                          e.printStackTrace();   
                  } catch (BadPaddingException e) {   
                          e.printStackTrace();   
                  }   
                  return null;   
          }   
          
          public static final String VIPARA = "0102030405060708";  
          public static final String bm = "UTF-8"; 
          
          public static String encryptWithAndroid(String dataPassword, String cleartext)  
        		  throws Exception {  
        	  IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());  
        	  SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(), "AES");  
        	  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
        	  cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);  
        	  byte[] encryptedData = cipher.doFinal(cleartext.getBytes(bm));  
        	  
        	  return StringUtil.parseByte2HexStr(encryptedData);  
          }  
          
          public static String decryptWithAndroid(String dataPassword, String encrypted)  
        		  throws Exception {  
        	  byte[] byteMi = StringUtil.parseHexStr2Byte(encrypted);  
        	  IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());  
        	  SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(), "AES");  
        	  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
        	  cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);  
        	  byte[] decryptedData = cipher.doFinal(byteMi);  
        	  
        	  return new String(decryptedData,bm);  
          } 
          
          
        
      public static void main(String[] args) throws Exception {  
          String content = "123"  ;
          String password = "1234567812345678"  ;   
          //加密   
         System.out.println("加密前：" + content);   
         byte[] encryptResult = encrypt(content, password);
         System.out.println(StringUtil.buildHexStringWithASCII(encryptResult));
         String tt4 = new String(Base64.encode(encryptResult));  
         System.out.println(new String(tt4));  
           
          //解密   
         byte[] decryptResult = decrypt(encryptResult,password);   
         System.out.println("解密后：" + new String(decryptResult)); 
         
//         System.out.println(new String(Base64.encode(encrypt2("123", "123"))));
         
         System.out.println(encryptWithAndroid("1234567812345678", "123"));
         System.out.println(decryptWithAndroid("1234567812345678", "eWH3PAw5RCFE8TCWiPA0PQ=="));
         
         
         
//         
//         byte[] decryptResult2 = decrypt("jrAOn8pgUin7bpmRhtSXzQ==\n".getBytes("UTF-8"),"123");   
//         System.out.println("解密后2：" + new String(decryptResult2)); 
         
         
    }  
}  
