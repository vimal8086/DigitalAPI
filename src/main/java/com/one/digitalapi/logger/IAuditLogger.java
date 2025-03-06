package com.one.digitalapi.logger;

import com.one.digitalapi.logger.vo.AuditVO;

public interface IAuditLogger {
	
	public void sendAuditRequest(AuditVO auditVO);
}
