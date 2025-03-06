package com.one.digitalapi.exception;


/**
 * The Enum ExceptionType.
 */
public enum DigitalAPIBusinessExceptionTypes implements ExceptionType{

	INVALID_INPUTS(1000L, "invalid.input", "INVALID_INPUTS"),
	MISSING_MANDATORY_PARAMETERS(1001L, "missing.mandatory", "MISSING_MANDATORY_PARAMETERS"),
	NOT_SUPPORTED(1002L, "not.supported", "NOT_SUPPORTED"),
	MISSING_CONFIGURATION(1003L, "missing.configuration", "MISSING_CONFIGURATION"),
	DATA_NOT_FOUND(1004L, "data.not.found", "DATA_NOT_FOUND"),
	INPUT_PARAM_NOT_MATCHING(1005L, "data.not.found", "INPUT_PARAM_NOT_MATCHING"),

	
	
	TECHNICAL_FAILURE(9999L, "technical.failure", "TECHNICAL_FAILURE")
	;

    private long errorCode;
	private String errorMsgId;
	private String errorCategory;

	DigitalAPIBusinessExceptionTypes(long code, String message, String category) {
        this.errorCode = code;
        this.errorMsgId = message;
        this.errorCategory = category;
    }

    /**
     *
     * @return
     */
    public String getMessageMsgId() {
        return errorMsgId;
    }

	public long getErrorCode() {
		return errorCode;
	}

	public String getErrorCategory() {
		return errorCategory;
	}

}
     
