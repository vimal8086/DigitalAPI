package com.one.digitalapi.logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class AuditProviderFactory {

	private static IAuditLogger auditLogger;
	private static String strLoggerFqn;

	private static final String CLASSNAME="AuditProviderFactory";
	private static final ILogger LOGGER = LogManager.getLogger();

	@Autowired
	private Environment env;

	private AuditProviderFactory(){
		//implementation
	}

	public static IAuditLogger getProviderInstance(){
		String strMethodName="getProviderInstance";
		if(auditLogger==null && strLoggerFqn!=null){
			try {
				Class<?> c = Class.forName(strLoggerFqn);
				Method factoryMethod = c.getDeclaredMethod("getInstance");
				auditLogger= (IAuditLogger) factoryMethod.invoke(null);
			}catch (InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException e){
				LOGGER.errorLog(CLASSNAME, strMethodName,"Problem in instantiation of audit FQN : ",e);
			}	
			if(auditLogger==null){
				LOGGER.errorLog(CLASSNAME, strMethodName,"Logger FQN is not configuring in properties file so initializing default audit logger com.one.digitalapi.logger.AuditLogger ");
				AuditProviderFactory.configure("com.one.digitalapi.logger.AuditLogger");
			}
		}
		return auditLogger;
	}

	private static synchronized void getProviderInstance(String strMethodName) {

		if(auditLogger==null){
			AuditProviderFactory.configure("com.one.digitalapi.logger.AuditLogger");
		}
	}

	public static void configure(String fqn){
		strLoggerFqn=fqn;
	}

	@PostConstruct
	public void init() {		
		AuditProviderFactory.configure("com.one.digitalapi.logger.AuditLogger");

	}
}
