package com.smtmvc.messageService.model;

import java.util.Date;

import com.smtmvc.messageService.model.enume.ConfirmStatus;

public class MessageConfirmRecord {
    private Long id;

    private String dr;

    private Date createDate;

    private Date updateDate;

    private String destination;

    private ConfirmStatus status;

    private Integer confirmTime;

    private String confirmUrl;

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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination == null ? null : destination.trim();
    }

    public ConfirmStatus getStatus() {
        return status;
    }

    public void setStatus(ConfirmStatus status) {
        this.status = status;
    }

    public Integer getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Integer confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getConfirmUrl() {
        return confirmUrl;
    }

    public void setConfirmUrl(String confirmUrl) {
        this.confirmUrl = confirmUrl == null ? null : confirmUrl.trim();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }
    
    
    /**
     * 通过消息,产生一个 确认记录
     * @param message
     * @return
     */
    public static MessageConfirmRecord newInstance(Message message ) {
    	MessageConfirmRecord messageConfirmRecord = new MessageConfirmRecord();
    	
    	messageConfirmRecord.setConfirmTime( message.getConfirmTime() );
    	messageConfirmRecord.setDestination( message.getDestination()  );
    	messageConfirmRecord.setUuid( message.getUuid() );
    	messageConfirmRecord.setConfirmUrl( message.getConfirmUrl() );
    	
		return messageConfirmRecord;
    }
}