package com.one.digitalapi.logger;

import com.one.digitalapi.logger.vo.AuditVO;

public class AuditUtility {

	private static IAuditLogger iAuditLogger = AuditProviderFactory.getProviderInstance();
	
	private AuditUtility(){
		//Implementation
	}
	
	public static void sendAuditRequest(AuditVO auditVO) {
		iAuditLogger.sendAuditRequest(auditVO);
	}
	
}
