package com.smtmvc.messageService.model;

import java.util.Date;

import com.smtmvc.messageService.model.enume.SendStatus;

public class MessageSendRecord {
    private Long id;

    private String dr;

    private Date createDate;

    private Date updateDate;

    private SendStatus status;

    private Integer sendTime;

    private String uuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr == null ? null : dr.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public SendStatus getStatus() {
        return status;
    }

    public void setStatus(SendStatus status) {
        this.status = status;
    }

    public Integer getSendTime() {
        return sendTime;
    }

    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }
    
    /**
     * 通过消息创建一个模板
     * @param message
     * @return
     */
	public static MessageSendRecord newInstance(Message message) {
		
		MessageSendRecord messageSendRecord = new MessageSendRecord();
		messageSendRecord.setSendTime( message.getSendTime() );
		messageSendRecord.setUuid( message.getUuid() );
		
		return messageSendRecord;
	}
}