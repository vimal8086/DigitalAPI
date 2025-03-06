package com.one.digitalapi.exception;

public interface ExceptionType {

	String getMessageMsgId();
	long getErrorCode();
	String getErrorCategory();
}
