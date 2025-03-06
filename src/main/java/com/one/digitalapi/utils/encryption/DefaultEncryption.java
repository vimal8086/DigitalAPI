package com.one.digitalapi.utils.encryption;


public class DefaultEncryption implements IEncryption {
	
	public String crypt(String enteredPassword) {
		return enteredPassword;
	}
	
	public boolean matches(String encryptedPassword, String enteredPassword) {
		return false;
	}
	
	public String decrypt(String encryptedPassword){
		
		return encryptedPassword;
	}
}