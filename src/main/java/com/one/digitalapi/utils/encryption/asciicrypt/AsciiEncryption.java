package com.one.digitalapi.utils.encryption.asciicrypt;

import com.one.digitalapi.logger.ConsoleLogger;
import com.one.digitalapi.utils.encryption.DefaultEncryption;

public class AsciiEncryption extends DefaultEncryption {
	
	private static ConsoleLogger logger = new ConsoleLogger();
	private static final String CLASSNAME = "AsciiEncryption";

  public String crypt(String enteredPassword){
	  
		return EncryptDecrypt.encrypt(enteredPassword);
  }
  
  /**
   * arg1 encryptedPassword
   * arg2 enteredPassword
   * returns true if the encryptedPassword and the enteredPassword matches 
   *		 false if the encryptedPassword and the enteredPassword does not match
   */

	public boolean matches(String encryptedPassword, String enteredPassword){
		
		boolean bValid = false;
		if(encryptedPassword != null && enteredPassword != null) {
			try {
				String decryptPassword = decrypt(encryptedPassword);
				if(decryptPassword != null) {
					bValid = decryptPassword.equals(enteredPassword);
				}
			}catch(Exception e) {
	    		logger.errorLog(CLASSNAME, "matches()", "Default Key Found.", e);
			}
		}
		return bValid;
	}
	
	// if decrypt fails it returns null
	public String decrypt(String encryptedPassword){
		
		String decryptPassword = null;
		try {
			decryptPassword = EncryptDecrypt.decrypt(encryptedPassword);
		}catch(Exception e) {
    		logger.errorLog(CLASSNAME, "decrypt()", "Default Key Found.", e);
		}
		return decryptPassword;
	}
}