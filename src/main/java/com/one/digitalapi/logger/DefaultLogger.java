package com.one.digitalapi.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLogger implements ILogger {
    
    private final Logger logger;
    protected static final String CLOSED_SQUARE_BRACES = " ] : ";
	protected static final String OPEN_SQUARE_BRACES = " [ ";
	private static final String SEMICOLON = ";";	
	private static final String HASH = "#";
	private static final String COLON = ":";
    
    private static ThreadLocal<StringBuilder> threadLocal = new ThreadLocal<StringBuilder>(){
		@Override
		protected StringBuilder initialValue(){
			return new StringBuilder(1000);
		}
	};
    
    public DefaultLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }
    
    public DefaultLogger(String clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }
    
    private String formatMessage(String className, String methodName, String msg) {
        return String.format(OPEN_SQUARE_BRACES + className + CLOSED_SQUARE_BRACES + OPEN_SQUARE_BRACES + methodName + CLOSED_SQUARE_BRACES + msg);

    }

    @Override
    public void infoLog(String strClassName, String strMethodName, String msg) {
        logger.info(formatMessage(strClassName, strMethodName, msg));
    }

    @Override
    public void warnLog(String strClassName, String strMethodName, String msg) {
        logger.warn(formatMessage(strClassName, strMethodName, msg));
    }

    @Override
    public void errorLog(String strClassName, String strMethodName, String msg) {
        logger.error(formatMessage(strClassName, strMethodName, msg));
    }

    @Override
    public void fatalLog(String strClassName, String strMethodName, String msg) {
        logger.error("FATAL: " + formatMessage(strClassName, strMethodName, msg));
    }

    @Override
    public void debugLog(String strClassName, String strMethodName, String msg) {
        logger.debug(formatMessage(strClassName, strMethodName, msg));
    }

    @Override
    public void debugLog(String strClassName, String strMethodName, String msg, long lResponseCode) {
        logger.debug(formatMessage(strClassName, strMethodName, msg) + " ResponseCode: " + lResponseCode);
    }

    @Override
    public void infoLog(String strClassName, String strMethodName, String msg, long lResponseCode) {
        logger.info(formatMessage(strClassName, strMethodName, msg) + " ResponseCode: " + lResponseCode);
    }

    @Override
    public void warnLog(String strClassName, String strMethodName, String msg, long lWarnCode) {
        logger.warn(formatMessage(strClassName, strMethodName, msg) + " WarnCode: " + lWarnCode);
    }

    @Override
    public void errorLog(String strClassName, String strMethodName, String msg, long lErrorCode) {
        logger.error(formatMessage(strClassName, strMethodName, msg) + " ErrorCode: " + lErrorCode);
    }

    @Override
    public void fatalLog(String strClassName, String strMethodName, String msg, long lErrorCode) {
        logger.error("FATAL: " + formatMessage(strClassName, strMethodName, msg) + " ErrorCode: " + lErrorCode);
    }

    @Override
    public void infoLog(String strClassName, String strMethodName, String msg, Throwable th) {
        logger.info(formatMessage(strClassName, strMethodName, msg), th);
    }

    @Override
    public void warnLog(String strClassName, String strMethodName, String msg, Throwable th) {
        logger.warn(formatMessage(strClassName, strMethodName, msg), th);
    }

    @Override
    public void errorLog(String strClassName, String strMethodName, String msg, Throwable th) {
        logger.error(formatMessage(strClassName, strMethodName, msg), th);
    }

    @Override
    public void fatalLog(String strClassName, String strMethodName, String msg, Throwable th) {
        logger.error("FATAL: " + formatMessage(strClassName, strMethodName, msg), th);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return logger.isErrorEnabled(); // SLF4J does not have a FATAL level, treating it as ERROR
    }

    @Override
    public String getLogLevelPriority() {
        return "DEBUG"; // SLF4J does not expose log level dynamically
    }

    @Override
    public void addMethodTime(String strClassName, String strMethodName, long millis) {
    	if(threadLocal.get().length() > 0)
			threadLocal.get().append(" <- [").append(strClassName).append(COLON).append(strMethodName).append(COLON).append(millis).append(CLOSED_SQUARE_BRACES);
		else
			threadLocal.get().append(OPEN_SQUARE_BRACES).append(strClassName).append(COLON).append(strMethodName).append(COLON).append(millis).append(CLOSED_SQUARE_BRACES);
    }

    @Override
    public void addMethodTime(String strMethodName, long millis) {
    	if(threadLocal.get().length() > 0) {
			threadLocal.get().append(SEMICOLON).append(strMethodName).append(HASH).append(millis);
		}else{
			threadLocal.get().append(strMethodName).append(HASH).append(millis);
		}
    }

    @Override
    public void addLog(String strMethodName) {
    	threadLocal.get().append(strMethodName);
    }

    @Override
    public void flushMethodTime(String className, String strMethodName, String eventName) {
    	warnLog(className, strMethodName," EVENTKPI-["+eventName+"]-"+threadLocal.get().toString());
		threadLocal.set(new StringBuilder(1000));
    }

    @Override
    public void flushMethodTime() {
    	threadLocal.set(new StringBuilder(1000));
    }

    @Override
    public void addAuditWarnLog(String strClassName, String strMethodName, String message) {
    	logger.warn(OPEN_SQUARE_BRACES + strClassName + CLOSED_SQUARE_BRACES + OPEN_SQUARE_BRACES + strMethodName + CLOSED_SQUARE_BRACES + message);
    }
}
