package com.one.digitalapi.logger;

public interface ILogger {

	/**
	 * Add Info Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 */
	void infoLog(String strClassName, String strMethodName, String msg);

	/**
	 * Add Warn Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 */
	void warnLog(String strClassName, String strMethodName, String msg);

	/**
	 * Add Error Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 */
	void errorLog(String strClassName, String strMethodName, String msg);

	/**
	 * Add Fatal Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 */
	void fatalLog(String strClassName, String strMethodName, String msg);

	/**
	 * Add Debug Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 */
	void debugLog(String strClassName, String strMethodName, String msg);

	/**
	 * Add Debug Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 * @param lResponseCode
	 */
	void debugLog(String strClassName, String strMethodName, String msg, long lResponseCode);

	/**
	 * Add Info Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 * @param lResponseCode
	 */
	void infoLog(String strClassName, String strMethodName, String msg, long lResponseCode);

	/**
	 * Add Warn Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 * @param lWarnCode
	 */
	void warnLog(String strClassName, String strMethodName, String msg, long lWarnCode);

	/**
	 * Add Error Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 * @param lErrorCode
	 */
	void errorLog(String strClassName, String strMethodName, String msg, long lErrorCode);

	/**
	 * Add Fatal Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 * @param lErrorCode
	 */
	void fatalLog(String strClassName, String strMethodName, String msg, long lErrorCode);

	/**
	 * Add Info Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 * @param throwable
	 */
	void infoLog(String strClassName, String strMethodName, String msg, Throwable th);

	/**
	 * Add Warn Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 * @param throwable
	 */
	void warnLog(String strClassName, String strMethodName, String msg, Throwable th);

	/**
	 * Add Error Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 * @param throwable
	 */
	void errorLog(String strClassName, String strMethodName, String msg, Throwable th);

	/**
	 * Add Fatal Log based on given details
	 * @param strClassName
	 * @param strMethodName
	 * @param msg
	 * @param throwable
	 */
	void fatalLog(String strClassName, String strMethodName, String msg, Throwable th);

	/**
	 * Get Log Level Priority
	 * @return Log Level Priority
	 */
	String getLogLevelPriority();

	/**
	 * Check if debug is enabled
	 * @return isDebugEnabled
	 */
	public boolean isDebugEnabled();

	/**
	 * Check if Info is enabled
	 * @return isInfoEnabled
	 */
	public boolean isInfoEnabled();

	/**
	 * Check if Warn is enabled
	 * @return isWarnEnabled
	 */
	public boolean isWarnEnabled();

	/**
	 * Check if Error is enabled
	 * @return isErrorEnabled
	 */
	public boolean isErrorEnabled();

	/**
	 * Check if Fatal is enabled
	 * @return isFatalEnabled
	 */
	public boolean isFatalEnabled();
	
	/**
	 * Add KPI Log
	 * @param strClassName
	 * @param strMethodName
	 * @param millis
	 */
	public void addMethodTime(String strClassName ,String strMethodName, long millis);

	/**
	 * Add KPI Log
	 * @param strMethodName
	 * @param millis
	 */
	public void addMethodTime(String strMethodName, long millis);
	
	/**
	 * Add KPI Log
	 * @param strMethodName
	 */
	public void addLog(String strMethodName);
	
	/**
	 * print KPI Log
	 * @param className
	 * @param strMethodName
	 * @param eventName
	 */
	public void flushMethodTime(String className, String strMethodName, String eventName);
	
	/**
	 * Initialize KPI Log
	 */
	public void flushMethodTime();
	/**
	 * Audit logs to be printed in warn level
	 */
	public void addAuditWarnLog(String strClassName, String strMethodName,String message);

}
