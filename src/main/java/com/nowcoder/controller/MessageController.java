package com.nowcoder.controller;


import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static  final Logger logger = LoggerFactory.getLogger(MessageController.class) ;


    @Autowired
    MessageService messageService ;
    @Autowired
    UserService userService ;

    @Autowired
    HostHolder hostHolder ;


    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String getConversationList(Model model){
        try {
            int localHostId = hostHolder.getUser().getId() ;
            List<Message> conversationList = messageService.getConversationList(localHostId,0,10) ;
            List<ViewObject> conversations = new ArrayList<ViewObject>() ;
            for (Message msg : conversationList){
                ViewObject viewObject = new ViewObject() ;
                viewObject.set("conversation",msg);
                int targetId = msg.getFromId()==localHostId ? msg.getToId() : msg.getFromId() ;
                User user = userService.getUser(targetId) ;
                viewObject.set("user",user);
                viewObject.set("unread", messageService.getConversationUnread(localHostId, msg.getConversationId())) ;
                conversations.add(viewObject) ;
            }
            model.addAttribute("conversations",conversations) ;
        }catch (Exception e){
            logger.error("获取信息失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"获取会话列表错误");
        }
        return "letter" ;
    }

    @RequestMapping(path={"/msg/detail"},method = {RequestMethod.GET})
    public String getConversationDetail(Model model, @Param("conversationId") String conversationId){
        try{
            List<Message> conversationList = messageService.getConversationDetail(conversationId,
                    0,10) ;
            List<ViewObject> messages = new ArrayList<ViewObject>() ;
            for (Message msg : conversationList){
                ViewObject vo = new ViewObject();
                vo.set("message",msg);
                User user = userService.getUser(msg.getFromId()) ;
                if (user == null) {
                    continue;
                }
                vo.set("user",user);
                vo.set("headUrl", user.getHeadUrl()) ;
                vo.set("userId",user.getId());
                messages.add(vo) ;
            }
            model.addAttribute("messages",messages) ;
        }catch (Exception e){
            logger.error("获取站内信错误"+e.getMessage());
        }
        return "letterDetail" ;
    }

    @RequestMapping(path = {"/msg/addMessage/"},method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                          @RequestParam("toId") int toId,
                          @RequestParam("content") String content){
        try {
            Message message = new Message();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setHasRead(0);
            message.setConversationId(fromId<toId ? String.format("%d_%d",fromId,
                    toId) : String.format("%d_%d",toId, fromId));
            messageService.addMessage(message) ;
        }catch (Exception e){
            logger.error("添加站内信失败"+ e.getMessage());
        }
        return ToutiaoUtil.getJSONString(0) ;
    }


}
