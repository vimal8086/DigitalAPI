package com.one.digitalapi.logger;

import java.util.HashMap;
import java.util.Map;

public enum LogLevel {

	NONE(0), ERROR(1), WARN(2), INFO(3), DEBUG(4), TRACE(5), ALL(6);

	public final int level;
	private static Map<Integer, LogLevel> logLevels;

	static {
		logLevels = new HashMap<>();
		for (LogLevel logLevel : values()) {
			logLevels.put(Integer.valueOf(logLevel.level), logLevel);
		}
	}

	private LogLevel(int level) {
		this.level = level;
	}

	public static LogLevel fromLogLevel(int level) {
		return (LogLevel) logLevels.get(Integer.valueOf(level));
	}
}
