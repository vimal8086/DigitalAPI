package com.one.digitalapi.utils.encryption.aes;

import java.io.File;
import java.nio.charset.Charset;
import java.security.Key;
import java.util.Base64;
import java.util.ResourceBundle;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.one.digitalapi.logger.ConsoleLogger;
import com.one.digitalapi.utils.DigitalAPIConstant;


public class AESEncrypt
{
	private static final String CLASSNAME = "AESEncrypt";
    private static final String algorithm = "AES";
    private static final byte[] keyValue = new byte[] { 'E', 'L', '1', '2', 'e', 'c', '@', 'R', 'E', 't', 'e', 'L', 'e', '(', '0', 'M' };
    private static String aesPropertyFile = "aes_encrypt";    
    private static byte[] propKeyValue = keyValue;
    
    private static ConsoleLogger logger = new ConsoleLogger();
    
    static{
    	try{
    		File f = new File(aesPropertyFile);
    		if(f.exists()){
	    		ResourceBundle aesProperties = ResourceBundle.getBundle(aesPropertyFile);
	    		if(aesProperties!=null && aesProperties.containsKey(DigitalAPIConstant.ENCRYPT_KEY_VALUE)){
	    			propKeyValue = aesProperties.getString(DigitalAPIConstant.ENCRYPT_KEY_VALUE).getBytes(Charset.forName(DigitalAPIConstant.DEFAULT_CHARSET));
	    			logger.infoLog(CLASSNAME, "STATIC_BLOCK", "key found in property file.");
	    		}
    		}
    	}catch(Exception e){    		
    		logger.errorLog(CLASSNAME, "STATIC_BLOCK", "Default Key Found.", e);
    	}
    }
    // Performs Encryption
    public static String encrypt(String plainText)
    {
	byte[] encVal = null;
	try
	{
	    Key key = generateKey();
	    Cipher chiper = Cipher.getInstance(algorithm);
	    chiper.init(Cipher.ENCRYPT_MODE, key);
	    encVal = chiper.doFinal(plainText.getBytes(Charset.forName(DigitalAPIConstant.DEFAULT_CHARSET)));	    
	}
	catch (Exception e)
	{
		logger.errorLog(CLASSNAME, "encrypt()", "Default Key Found.", e);
	}
	String encryptedValue = Base64.getEncoder().encodeToString(encVal);
	return encryptedValue;
    }

    // Used to for Decryption
    public static String decrypt(String encryptedText)
    {
	String decryptedValue = null;
	try
	{
	    // generate key
	    Key key = generateKey();
	    Cipher chiper = Cipher.getInstance(algorithm);
	    chiper.init(Cipher.DECRYPT_MODE, key);
	    byte[] decordedValue = Base64.getDecoder().decode(encryptedText);
	    byte[] decValue = chiper.doFinal(decordedValue);
	    decryptedValue = new String(decValue,DigitalAPIConstant.DEFAULT_CHARSET);//FindBug changes for Charset "UTF-8".
	}
	catch (Exception e)
	{
		logger.errorLog(CLASSNAME, "decrypt()", "Default Key Found.", e);
	}
	return decryptedValue;
    }

    // Used to generate a secret key for AES algorithm generateKey()
    private static Key generateKey()
    {
	Key key = new SecretKeySpec(propKeyValue, algorithm);
	return key;
    }
}
