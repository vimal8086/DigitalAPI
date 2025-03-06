package com.one.digitalapi.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class DigitalAPIResponse<T> implements Serializable
{
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private String errorCategory;
	private String responseMessage;
	private long responseCode;
	private T responseObject;

	public DigitalAPIResponse(long responseCode, String responseMessage, String errorCategory, T responseObject) {
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.errorCategory = errorCategory;
		this.responseObject = responseObject;
	}
	
	public DigitalAPIResponse(long responseCode, T responseObject, String errorCategory)
	{
		this.responseCode =responseCode;
		this.responseObject = responseObject;
		this.errorCategory = errorCategory;
	}
	
	public DigitalAPIResponse()
	{
		
	}
	
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	public long getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(long responseCode) {
		this.responseCode = responseCode;
	}
	public T getResponseObject() {
		return responseObject;
	}
	public void setResponseObject(T responseObject) {
		this.responseObject = responseObject;
	}

	public String getErrorCategory() {
		return errorCategory;
	}

	public void setErrorCategory(String errorCategory) {
		this.errorCategory = errorCategory;
	}

	public boolean isError() {
		if( this.responseCode >= 0 )
			return false;
		else
			return true;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Response{");
		sb.append("errCategory='").append(errorCategory).append('\'');
		sb.append(", responseMessage='").append(responseMessage).append('\'');
		sb.append(", responseCode=").append(responseCode);
		sb.append(", responseObject=").append(responseObject);
		sb.append('}');
		return sb.toString();
	}

}