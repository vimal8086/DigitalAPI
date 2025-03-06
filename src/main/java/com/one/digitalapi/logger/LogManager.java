package com.one.digitalapi.logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LogManager {
	
	private static ILogger defaultLogger;
	private static Map<String, ILogger> keyToLogger = new ConcurrentHashMap<>();
	
	static {
		if (defaultLogger == null) {
			defaultLogger = new ConsoleLogger();
		}
	}

	/**
	 * Set default logger
	 * @param dfaultLogger
	 */
	public static void setDefaultLogger(ILogger dfaultLogger){
		if(null == dfaultLogger) {
			throw new NullPointerException("defaultLogger is null");
		}
		defaultLogger = dfaultLogger; 
	}
	
	/**
	 * Set logger for the provided key
	 * @param key
	 * @param logger
	 */
	public static void setLogger(String key, ILogger logger){
		if(null == key){
			throw new NullPointerException("key for logger is null");
		}
		if(null == logger) {
			throw new NullPointerException("logger is null");
		}
		keyToLogger.put(key, logger);
	}
	
	/**
	 * Get default logger
	 * @return ILogger
	 */
	public static ILogger getLogger(){
	    return defaultLogger;
	}
	
	/**
	 * Get logger based on the provided key
	 * @param key 
	 * @return ILogger
	 */
	public static ILogger getLogger(String key){
	    return (ILogger)keyToLogger.get(key);
	}
	
}
