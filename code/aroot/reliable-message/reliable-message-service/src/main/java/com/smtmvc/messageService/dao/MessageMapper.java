package com.smtmvc.messageService.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.smtmvc.messageService.model.Message;
import com.smtmvc.messageService.model.enume.MessageStatus;

public interface MessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKeyWithBLOBs(Message record);

    int updateByPrimaryKey(Message record);
    
    /**
     * 确认发送
     * @param messageUUID
     * @param waitingSend 
     */
	void confirmSend(String messageUUID, MessageStatus waitingSend);

	/**
	 * 通过UUID查询
	 * @author ZHANGYUKUN
	 * @param messageUUID
	 * @return
	 */
	Message selectByUUID(String messageUUID);
	
	/**
	 * 修改状态
	 * @author ZHANGYUKUN
	 * @param messageUUID
	 * @param waitingSend
	 * @return
	 */
	int changeStatus(@Param("uuid") String uuid,@Param("status") MessageStatus status);

	
	/**
	 * 查询某个状态的 消息
	 * @param status
	 * @return
	 */
	List<Message> queryByStatus(MessageStatus status);

	
	/**
	 * 通过UUID删除
	 * @param uuid
	 * @return
	 */
	int deleteByUUID(String uuid);

	
	/**
	 * 询问次数+1
	 * @param uuid
	 */
	void addConfirmTime(String uuid);

	
	/**
	 * 发送次数+1
	 * @param uuid
	 */
	void addSendTime(String uuid);

	
	/**
	 * 查询已发送,通过发送次数查询
	 * @param status
	 * @param sendTime
	 * @return
	 */
	List<Message> querySendedByStatus(@Param( "status") MessageStatus status,@Param("sendTime") int sendTime);
	
	/**
	 * 查询待确认,通过发送次数查询
	 * @param status
	 * @return
	 */
	List<Message> queryWaitingConfirmByStatus(@Param( "status") MessageStatus status,@Param("confirmTime") int confirmTime);



}