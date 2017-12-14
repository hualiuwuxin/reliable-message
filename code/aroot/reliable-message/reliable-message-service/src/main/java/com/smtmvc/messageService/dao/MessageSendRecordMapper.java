package com.smtmvc.messageService.dao;

import com.smtmvc.messageService.model.MessageSendRecord;

public interface MessageSendRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(MessageSendRecord record);

    MessageSendRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageSendRecord record);

    int updateByPrimaryKey(MessageSendRecord record);

    /**
     * 插入发送记录
     * @param record
     * @return
     */
    int insert(MessageSendRecord record);
}