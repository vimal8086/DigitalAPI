package com.one.digitalapi.exception;

public class DigitalAPIException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private Long errorCode;
	private String errorMessage;
	private String errorCategory;

	public DigitalAPIException(Long errCode, String errMessage, String errorCategory) {
		super(errMessage);
		this.errorCode = errCode;
		this.errorMessage = errMessage;
		this.errorCategory = errorCategory;
	}
	
	public long getErrorCode() {
		return this.errorCode;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}

	public String getErrorCategory() {
		return errorCategory;
	}
}
