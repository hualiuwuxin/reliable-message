package com.smtmvc.messageService.dao;

import com.smtmvc.messageService.model.MessageConfirmRecord;

public interface MessageConfirmRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(MessageConfirmRecord record);

    MessageConfirmRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageConfirmRecord record);

    int updateByPrimaryKey(MessageConfirmRecord record);
    
    /**
     * 插入询问记录
     * @param record
     * @return
     */
    int insert(MessageConfirmRecord record);
}