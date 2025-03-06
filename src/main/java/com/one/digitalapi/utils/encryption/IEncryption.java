package com.one.digitalapi.utils.encryption;

public interface IEncryption {
	
	/**
	 * arg plainString
	 * returns the crypted String
	 */
	
	public String crypt(String enteredPassword);
  /**
   * arg1 encryptedPassword
   * arg2 enteredPassword
   * returns true if the encryptedPassword and the enteredPassword matches 
   *		 false if the encryptedPassword and the enteredPassword does not match
   */

	
	public boolean matches(String encryptedPassword, String enteredPassword); 
	
	public String decrypt(String encryptedPassword);

}

