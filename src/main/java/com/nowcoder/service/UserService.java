package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO ;


    public Map<String, Object> register(String username, String password)
    {
        Map<String, Object> map = new HashMap<String,Object>() ;
        if (StringUtils.isBlank(username))
        {
            map.put("msgusername","用户名不能为空") ;
            return map ;
        }
        if (StringUtils.isBlank(password))
        {
            map.put("msgpassword","密码不能为空") ;
            return map;
        }

        User user = userDAO.selectByName(username) ;
        if (user != null)
        {
            map.put("msgnotnull","该用户名已被注册");
            return map;
        }

        user = new User() ;
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().replaceAll("-",""));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user) ;

        //登陆
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket) ;
        return  map ;
    }


    public Map<String, Object> login(String username, String password)
    {
        Map<String, Object> map = new HashMap<String,Object>() ;
        if (StringUtils.isBlank(username))
        {
            map.put("msgusername","用户名不能为空") ;
            return map ;
        }
        if (StringUtils.isBlank(password))
        {
            map.put("msgpassword","密码不能为空") ;
            return map;
        }

        User user = userDAO.selectByName(username) ;
        if (user == null)
        {
            map.put("msgnotnull","用户不存在，请重新登录");
            return map;
        }

        /*
        **********************************用户存在，开始验证密码*******************************************
         */
        if (!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword()))
        {
            map.put("msgpwd","密码不正确");
            return  map ;
        }

        /*
        **********************************用户名密码验证无误后,给用户赋予ticket*******************************************
         */
      String ticket = addLoginTicket(user.getId());
      map.put("ticket",ticket) ;

        return  map ;
    }

    private  String addLoginTicket(int userId)
    {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date() ;
        date.setTime(date.getTime()+1000*3600*24);
        loginTicket.setExpired(date);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addLoginTicket(loginTicket) ;

        return loginTicket.getTicket();

    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
}
