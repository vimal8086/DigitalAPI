package com.one.digitalapi.utils.encryption.md5;

import com.one.digitalapi.utils.encryption.DefaultEncryption;

public class MD5Encryption extends DefaultEncryption {

	public String crypt(String unencryptedPassword){
		
		return MD5.crypt(unencryptedPassword);
	}
	
	public boolean matches(String encryptedPassword, String unecyrptedPassword){
		return crypt(unecyrptedPassword).equals(encryptedPassword);
	}

}
