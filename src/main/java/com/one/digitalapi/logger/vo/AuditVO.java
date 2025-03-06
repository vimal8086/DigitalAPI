package com.one.digitalapi.logger.vo;

public class AuditVO {

	private String eventTime;
	private String eventType;
	private String eventID;
	private String remark;
	private String oldValue;
	private String newValue;
	private String userName;
	private String ipAddress;
	
	public String getEventTime() {
		return eventTime;
	}
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventID() {
		return eventID;
	}
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	@Override
	public String toString() {
		return "AuditVO [eventTime=" + eventTime + ", eventType=" + eventType + ", eventID=" + eventID + ", remark="
				+ remark + ", oldValue=" + oldValue + ", newValue=" + newValue + ", userName=" + userName
				+ ", ipAddress=" + ipAddress + "]";
	}
	
}
