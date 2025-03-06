 package com.one.digitalapi.utils.encryption;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.one.digitalapi.exception.DigitalAPIException;
import com.one.digitalapi.exception.DigitalAPIExceptionHelper;
import com.one.digitalapi.logger.ConsoleLogger;
import com.one.digitalapi.utils.DigitalAPIConstant;


public class PasswordEncryption{

  public static final int NONE = 0;	  
  public static final int UNIX_CRYPT = 1;
  public static final int MD5 = 2;
  public static final int ASCIICRYPT = 4;
  public static final int AES_128_CRYPT = 5;  
  public static final String STR_NONE = "NONE";	  
  public static final String STR_UNIX_CRYPT = "UNIX_CRYPT";
  public static final String STR_MD5 = "MD5";
  public static final String STR_ASCIICRYPT = "ASCIICRYPT";
  public static final String STR_AES_128_CRYPT = "AES_128_CRYPT";
  public static final String NONE_ENCRYPTION_TYPE_ID = "ENT01";	  
  public static final String UNIX_CRYPT_ENCRYPTION_TYPE_ID = "ENT02";
  public static final String ELITE_CRYPT_ENCRYPTION_TYPE_ID = "ENT03";
  public static final String MD5_CRYPT_ENCRYPTION_TYPE_ID = "ENT04";
  public static final String AES_CRYPT_ENCRYPTION_TYPE_ID = "ENT05";
  public static final String PREFIX_NONE = "{none}";
  public static final String PREFIX_CRYPT = "{crypt}";
  public static final String PREFIX_MD5 = "{md5}";
  public static final String PREFIX_ELITECRYPT = "{elitecrypt}";
  public static final String PREFIX_ASCII = "{ascii}";
  public static final String PREFIX_AES_128 = "{aes128}";
  
  private PasswordEncryption(){
	
  }
  
  private static ConsoleLogger logger = new ConsoleLogger();

  /**
   *	Returns encrypted string
   *	arg1 unecrypted string
   *	arg2 type of encryption
   */	  
  public  static final String crypt(String unencryptedPassword,String strAlias){
	 return PasswordEncryption.getCrypt(getEncryptionTypeFromAlias(strAlias)).crypt(unencryptedPassword);
  }
  
  /**
   * Crypt string list with given encryption type
   * @param unencryptedPasswordList
   * @param strAlias
   * @return
   */
  public static final List<String> crypt(List<String> unencryptedPasswordList,String strAlias){
	  List<String> encryptedPasswordList = new ArrayList<>();
	  int encType = PasswordEncryption.getEncryptionTypeFromAlias(strAlias);
	  IEncryption encryption = getCrypt(encType);
	  for(String password : unencryptedPasswordList){
		  String s = encryption.crypt(password);
		  encryptedPasswordList.add(s);
	  }
	  return encryptedPasswordList;
  }
  
  /**
   *	Returns int For CorresPonding Alias
   *	arg1 Alias string
  */
   public static final int getEncryptionTypeFromAlias(String strAlias) {
	if(strAlias.equalsIgnoreCase(STR_UNIX_CRYPT) )
	{
		return UNIX_CRYPT;
	}
	else if(strAlias.equalsIgnoreCase(STR_MD5)) 
	{
		return MD5;
	}
	else if(strAlias.equalsIgnoreCase(STR_ASCIICRYPT))
	{
		return ASCIICRYPT;
	}
	else if(strAlias.equalsIgnoreCase(STR_AES_128_CRYPT))
	{
		return AES_128_CRYPT;
	}
	else
	{
		return NONE;
	}
	
  }

  /**
   *	Returns encrypted string
   *    arg1 encryptedPassword String
   *	arg2 unencryptedPassword string
   *	arg3 Alias of encryption
   */
  public static final boolean matches(String encryptedPassword, String unencryptedPassword,String strAlias) {
	  
		return PasswordEncryption.getCrypt(getEncryptionTypeFromAlias(strAlias)).matches(encryptedPassword,unencryptedPassword);
  }
  
  public static final String decrypt(String encryptedPassword, String strAlias){
	  
		return PasswordEncryption.getCrypt(getEncryptionTypeFromAlias(strAlias)).decrypt(encryptedPassword);
  }
	
  private static IEncryption getCrypt(int type){
	  
	String class_name= null;
	if ( type == MD5)
		class_name = "com.one.digitalapi.utils.encryption.md5.MD5Encryption";
	else if ( type == NONE ) 
		class_name = "com.one.digitalapi.utils.encryption.plaintext.PlainTextEncryption";
	else if ( type == ASCIICRYPT ) 
		class_name = "com.one.digitalapi.utils.encryption.asciicrypt.AsciiEncryption";
	else if ( type == AES_128_CRYPT ) 
		class_name = "com.one.digitalapi.utils.encryption.aes.AESEncryption";	
	else
	{
		throw DigitalAPIExceptionHelper.throwExceptionWithTemplate(DigitalAPIException.class, DigitalAPIConstant.AUTHENTICATION_ERROR, "Encyption type is not supported.");
	}	
	IEncryption  icrypt = null;
	
	try {
		icrypt = (IEncryption)Class.forName(class_name).getDeclaredConstructor().newInstance();
	}
	catch(java.lang.ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e){
		logger.errorLog("PasswordEncryption" , "getCrypt()", " ClassNotFoundException occured in getCrypt()", e);
		throw DigitalAPIExceptionHelper.throwExceptionWithTemplate(DigitalAPIException.class, DigitalAPIConstant.AUTHENTICATION_ERROR, e.getMessage());
	}
	return icrypt;
  }
  
  public static void main (String[] args) {
	  String resultStr = null;
	  
	  try {
		  if("encrypt".equalsIgnoreCase(args[0])) {
			  resultStr = crypt(args[1], args[2]);
		  }
		  else if("decrypt".equalsIgnoreCase(args[0])) {
			  resultStr = decrypt(args[1], args[2]);
		  }
	  }
	  catch(Exception ex) {
	  }
	  System.out.println(resultStr);
  }
  
}