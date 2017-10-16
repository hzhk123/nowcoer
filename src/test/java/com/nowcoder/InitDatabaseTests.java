package com.nowcoder;

import com.nowcoder.dao.*;
import com.nowcoder.model.*;
import com.nowcoder.util.ToutiaoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;

    @Autowired
    NewsDAO newsDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    CommentDao commentDao ;

    @Autowired
    MessageDao messageDao ;

    @Test
    public void initData() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setSalt(UUID.randomUUID().toString().replaceAll("-",""));
            user.setPassword(ToutiaoUtil.MD5("111111"+user.getSalt()));
            userDAO.addUser(user);

            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsDAO.addNews(news);
            for (int j=0; j<3; j++){
                Comment comment = new Comment();
                comment.setUserId(i+1);
                comment.setEntityType(EntityType.ENTITY_NEWS);
                comment.setCreatedDate(new Date());
                comment.setStatus(0);
                comment.setContent("这条评论来自于 %d" +String.valueOf(j));
                comment.setEntityId(news.getId());
                commentDao.addComment(comment) ;
            }

            userDAO.updatePassword(user);
//
//            LoginTicket ticket = new LoginTicket();
//            ticket.setStatus(0);
//            ticket.setUserId(i+1);
//            ticket.setExpired(date);
//            ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
//            loginTicketDAO.addLoginTicket(ticket);
//
//            loginTicketDAO.updateStatus(ticket.getTicket(), 2);

        }
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));

    }

}
