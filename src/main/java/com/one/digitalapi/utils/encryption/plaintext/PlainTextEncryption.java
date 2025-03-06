package com.one.digitalapi.utils.encryption.plaintext;

import com.one.digitalapi.utils.encryption.DefaultEncryption;

public class PlainTextEncryption extends DefaultEncryption { 
	
  public String crypt(String enteredPassword){
		return enteredPassword;
  }
  /**
   * arg1 encryptedPassword
   * arg2 enteredPassword
   * returns true if the encryptedPassword and the enteredPassword matches 
   *		 false if the encryptedPassword and the enteredPassword does not match
   */

	
	public boolean matches(String encryptedPassword, String enteredPassword){
		return encryptedPassword.equals(enteredPassword); 
	}
	
	public String decrypt(String enteredPassword){
		return enteredPassword;
  }

}