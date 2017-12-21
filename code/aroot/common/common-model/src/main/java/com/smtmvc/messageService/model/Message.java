package com.smtmvc.messageService.model;

import java.io.Serializable;
import java.util.Date;

import com.smtmvc.messageService.model.enume.MessageStatus;

public class Message implements Serializable {


	private static final long serialVersionUID = -7482266728146094335L;

	private Long id;

    private String dr;

    private Date createDate;

    private Date updateDate;
    
    private String destination;

    private String type;

    private MessageStatus status;

    private Integer sendTime;

    private Integer confirmTime;

    private String confirmUrl;

    private String uuid;

    private String content;

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


    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Integer getSendTime() {
        return sendTime;
    }

    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Message [id=" + id + ", dr=" + dr + ", createDate=" + createDate + ", updateDate=" + updateDate
				+ ", destination=" + destination + ", type=" + type + ", status=" + status + ", sendTime=" + sendTime
				+ ", confirmTime=" + confirmTime + ", confirmUrl=" + confirmUrl + ", uuid=" + uuid + ", content="
				+ content + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	public static Message newInstance() {
		Message message = new Message();
		message.setType("queue");
		message.setSendTime(0);
		message.setConfirmTime(0);
		message.setStatus( MessageStatus.WAITING_CONFIRM );
		
		
		return message;
	}
	
	
	public static Message newInstance(String destination ,String confirmUrl ,String content,String uuid) {
		Message message = new Message();
		message.setType("queue");
		message.setSendTime(0);
		message.setConfirmTime(0);
		message.setStatus( MessageStatus.WAITING_CONFIRM );
		
		
		message.setDestination(destination );
		message.setConfirmUrl(confirmUrl );
		message.setContent(  content );
		message.setUuid( uuid );
		
		return message;
	}
	
	
}