package com.one.digitalapi.logger;

import java.io.Serializable;

import com.one.digitalapi.logger.vo.AuditVO;


public class AuditLogger implements IAuditLogger, Serializable{

	private static final long serialVersionUID = 1L;

	private static final String CLASSNAME="AuditLogger";

	private static final DefaultLogger LOGGER = new DefaultLogger(AuditLogger.class);
	private static AuditLogger auditLogger = null;
	private static ILogger auditFileLogger = null;
	private static final String AUDITLOGSEPARATOR = "#";
	private static final String AUDITLOGDEFAULTVALUE = "-";
	private static final String AUDITLOGSEPARATOREND = ";";
	
	private AuditLogger(){
		//Singleton implementation
	}

	/** 
	 * This method returns instance of this class if already created else create new.
	 * @return AuditLogger
	 */
	public static synchronized AuditLogger getInstance(){
		String strMethodName="getInstance";
		if(auditLogger == null){
			try{
				LogManager.setLogger("AUDIT", new DefaultLogger("AUDIT"));
				auditFileLogger =  LogManager.getLogger("AUDIT");
				auditLogger = new AuditLogger();
			}catch(Exception e){
				LOGGER.errorLog(CLASSNAME, strMethodName, "Exception occurred in initialization of audit logger instance "+ e);
			}
		}
		return auditLogger;
	}

	@Override
	public void sendAuditRequest(AuditVO auditVO) {
		StringBuilder sb = new StringBuilder(300);
		sb.append("EventTime").append(AUDITLOGSEPARATOR).append(auditVO.getEventTime()!=null?auditVO.getEventTime():AUDITLOGDEFAULTVALUE).append(AUDITLOGSEPARATOREND);
		sb.append("EventType").append(AUDITLOGSEPARATOR).append(auditVO.getEventType()!=null?auditVO.getEventType():AUDITLOGDEFAULTVALUE).append(AUDITLOGSEPARATOREND);
		sb.append("EventId").append(AUDITLOGSEPARATOR).append(auditVO.getEventID()!=null?auditVO.getEventID():AUDITLOGDEFAULTVALUE).append(AUDITLOGSEPARATOREND);
		sb.append("Remark").append(AUDITLOGSEPARATOR).append(auditVO.getRemark()!=null?auditVO.getRemark():AUDITLOGDEFAULTVALUE).append(AUDITLOGSEPARATOREND);
		sb.append("OldValue").append(AUDITLOGSEPARATOR).append(auditVO.getOldValue()!=null?auditVO.getOldValue():AUDITLOGDEFAULTVALUE).append(AUDITLOGSEPARATOREND);
		sb.append("NewValue").append(AUDITLOGSEPARATOR).append(auditVO.getNewValue()!=null?auditVO.getNewValue():AUDITLOGDEFAULTVALUE).append(AUDITLOGSEPARATOREND);
		sb.append("Username").append(AUDITLOGSEPARATOR).append(auditVO.getUserName()!=null?auditVO.getUserName():AUDITLOGDEFAULTVALUE).append(AUDITLOGSEPARATOREND);
		sb.append("IPAddress").append(AUDITLOGSEPARATOR).append(auditVO.getIpAddress()!=null?auditVO.getIpAddress():AUDITLOGDEFAULTVALUE).append(AUDITLOGSEPARATOREND);
		auditFileLogger.addAuditWarnLog(CLASSNAME,"sendAuditRequest",sb.toString());
	}


}
