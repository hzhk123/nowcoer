package com.nowcoder.service;

import com.nowcoder.dao.MessageDao;
import com.nowcoder.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDao messageDao ;
    private  static  final Logger logger = LoggerFactory.getLogger(MessageService.class) ;

    public int addMessage(Message message){
        return messageDao.addMessage(message) ;
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDao.getConversationDetail(conversationId, offset, limit) ;
    }

    public List<Message> getConversationList(int userId, int offset, int limit){
        return  messageDao.getConversationList(userId, offset, limit) ;
    }

    public  int getConversationUnread(int userId, String conversationId) {
        return messageDao.getConversationUnreadCount(userId, conversationId) ;
    }
}
