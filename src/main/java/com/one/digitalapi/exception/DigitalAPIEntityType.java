package com.one.digitalapi.exception;


public enum DigitalAPIEntityType implements EntityType{

	AUTHENTICATION (101)
	;
	
	private long entityCode;
	
	DigitalAPIEntityType(long code){
		this.entityCode = code;
	}
	
	public long getEntityCode() {
		return entityCode;
	}
	
}
