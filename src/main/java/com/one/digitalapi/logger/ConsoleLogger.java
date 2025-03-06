package com.one.digitalapi.logger;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ConsoleLogger implements ILogger{
	protected static final String CODE = " Code : ";
	protected static final String CLOSED_SQUARE_BRACES = " ] : ";
	protected static final String OPEN_SQUARE_BRACES = " [ ";
	protected static final String MODULE_NAME = " [ONE DIGITAL API] : ";
	protected int errorLevel;
	protected static int stackTraceLogLevel = -1;

	/**
	 * Default Constructor
	 */
	public ConsoleLogger() {
		errorLevel = 0;
	}

	/**
	 * Set the level of Logging
	 */
	public void setLogLevelPriority(int logLevel) {}

	/**
	 * Get the level of Logging
	 */
	public String getLogLevelPriority() {
		return LogLevel.ALL.toString();
	}

	/**
	 * Set Error Level
	 * @param level
	 */
	public void setErrorLevel(int level) {
		errorLevel = level;
	}

	/**
	 * Get Error Level
	 * @return Error Level
	 */
	public int getErrorLevel() {
		return errorLevel;
	}

	/**
	 * Check if Trace is enabled
	 * @return isTraceEnabled
	 */
	public boolean isTraceEnabled() {
		return true;
	}

	/**
	 * Check if StackTrace is enabled
	 * @return isStackTraceEnabled
	 */

	public boolean isStackTraceEnabled() {
		return true;
	}

	/**
	 * Check if Debug is enabled
	 * @return isDebugEnabled
	 */
	public boolean isDebugEnabled() {
		return true;
	}

	/**
	 * Check if Info is enabled
	 * @return isInfoEnabled
	 */
	public boolean isInfoEnabled() {
		return true;
	}

	/**
	 * Check if Ward is enabled
	 * @return isWarnEnabled
	 */
	public boolean isWarnEnabled() {
		return true;
	}

	/**
	 * Check if Error is enabled
	 * @return isErrorEnabled
	 */
	public boolean isErrorEnabled() {
		return true;
	}

	/**
	 * Check if Fatal is enabled
	 * @return isFatalEnabled
	 */
	public boolean isFatalEnabled() {
		return true;
	}

	/**
	 * Add debug log based on given details
	 */
	public void debugLog(String strClassName, String strMethodName, String msg) {
		System.out.println(MODULE_NAME + OPEN_SQUARE_BRACES + strClassName + CLOSED_SQUARE_BRACES + OPEN_SQUARE_BRACES + strMethodName + CLOSED_SQUARE_BRACES + msg);
	}

	/**
	 * Add info log based on given details
	 */
	public void infoLog(String strClassName, String strMethodName, String msg) {
		System.out.println(MODULE_NAME + OPEN_SQUARE_BRACES + strClassName + CLOSED_SQUARE_BRACES + OPEN_SQUARE_BRACES + strMethodName + CLOSED_SQUARE_BRACES + msg);
		
	}

	/**
	 * Add warn log based on given details
	 */
	public void warnLog(String strClassName, String strMethodName, String msg) {
		System.out.println(MODULE_NAME + OPEN_SQUARE_BRACES + strClassName + CLOSED_SQUARE_BRACES + OPEN_SQUARE_BRACES + strMethodName + CLOSED_SQUARE_BRACES + msg);
	}

	/**
	 * Add error log based on given details
	 */
	public void errorLog(String strClassName, String strMethodName, String msg) {
		System.out.println(MODULE_NAME + OPEN_SQUARE_BRACES + strClassName + CLOSED_SQUARE_BRACES + OPEN_SQUARE_BRACES + strMethodName + CLOSED_SQUARE_BRACES + msg);
	}

	/**
	 * Add fatal log based on given details
	 */
	public void fatalLog(String strClassName, String strMethodName, String msg) {
		System.out.println(MODULE_NAME + OPEN_SQUARE_BRACES + strClassName + CLOSED_SQUARE_BRACES + OPEN_SQUARE_BRACES + strMethodName + CLOSED_SQUARE_BRACES + msg);
	}

	/**
	 * Add info log based on given details
	 */
	public void infoLog(String strClassName, String strMethodName, String msg, Throwable th) {
		System.err.println(getMessageAndStackTrace(strClassName, strMethodName, msg, th));
	}

	/**
	 * Add warn log based on given details
	 */
	public void warnLog(String strClassName, String strMethodName, String msg, Throwable th) {
		System.err.println(getMessageAndStackTrace(strClassName, strMethodName, msg, th));
	}

	/**
	 * Add error log based on given details
	 */
	public void errorLog(String strClassName, String strMethodName, String msg, Throwable th) {
		System.err.println(getMessageAndStackTrace(strClassName, strMethodName, msg, th));
	}

	/**
	 * Add fatal log based on given details
	 */
	public void fatalLog(String strClassName, String strMethodName, String msg, Throwable th) {
		System.err.println(getMessageAndStackTrace(strClassName, strMethodName, msg, th));
	}

	/**
	 * Add debug log based on given details
	 */
	public void debugLog(String strClassName, String strMethodName, String msg, long lResponseCode) {
		System.out.println(MODULE_NAME + OPEN_SQUARE_BRACES + strClassName + CLOSED_SQUARE_BRACES + OPEN_SQUARE_BRACES + strMethodName + CLOSED_SQUARE_BRACES + msg
					+ CODE + lResponseCode);
		
	}

	/**
	 * Add info log based on given details
	 */
	public void infoLog(String strClassName, String strMethodName, String msg, long lResponseCode) {
		System.out.println(MODULE_NAME + OPEN_SQUARE_BRACES + strClassName + CLOSED_SQUARE_BRACES + OPEN_SQUARE_BRACES + strMethodName + CLOSED_SQUARE_BRACES + msg
					+ CODE + lResponseCode);
	}

	/**
	 * Add warn log based on given details
	 */
	public void warnLog(String strClassName, String strMethodName, String msg, long lWarnCode) {
		System.out.println(MODULE_NAME + OPEN_SQUARE_BRACES + strClassName + CLOSED_SQUARE_BRACES + OPEN_SQUARE_BRACES + strMethodName + CLOSED_SQUARE_BRACES + msg
					+ CODE + lWarnCode);
	}

	/**
	 * Add error log based on given details
	 */
	public void errorLog(String strClassName, String strMethodName, String msg, long lErrorCode) {
		System.out.println(MODULE_NAME + OPEN_SQUARE_BRACES + strClassName + CLOSED_SQUARE_BRACES + OPEN_SQUARE_BRACES + strMethodName + CLOSED_SQUARE_BRACES + msg
					+ CODE + lErrorCode);
	}

	/**
	 * Add fatal log based on given details
	 */
	public void fatalLog(String strClassName, String strMethodName, String msg, long lErrorCode) {
		System.out.println(MODULE_NAME + OPEN_SQUARE_BRACES + strClassName + CLOSED_SQUARE_BRACES + OPEN_SQUARE_BRACES + strMethodName + CLOSED_SQUARE_BRACES + msg + CODE
				+ lErrorCode);
	}

	private String getMessageAndStackTrace(String strClassName, String strMethodName, String msg, Throwable exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.print(MODULE_NAME + OPEN_SQUARE_BRACES + strClassName + CLOSED_SQUARE_BRACES + OPEN_SQUARE_BRACES + strMethodName + CLOSED_SQUARE_BRACES + msg);
		exception.printStackTrace(pw); // NOSONAR
		return sw.toString();
	}

	@Override
	public void addMethodTime(String strClassName, String strMethodName, long millis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMethodTime(String strMethodName, long millis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addLog(String strMethodName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flushMethodTime(String className, String strMethodName, String eventName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flushMethodTime() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAuditWarnLog(String strClassName, String strMethodName, String message) {
		// TODO Auto-generated method stub
	}

}
