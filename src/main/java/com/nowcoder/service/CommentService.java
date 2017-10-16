package com.nowcoder.service;


import com.nowcoder.dao.CommentDao;
import com.nowcoder.model.Comment;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service
public class CommentService {


    @Autowired
    CommentDao commentDao ;
    private static final Logger logger = LoggerFactory.getLogger(Comment.class) ;

    public List<Comment> getCommnentByEntity(int entityId, int entityType){
        return commentDao.selectByEntity(entityId,entityType) ;
    }

    public int addComment(Comment comment){
        return commentDao.addComment(comment) ;
    }

    public int getCommnentCount(int entityId, int entityType){
        return  commentDao.getCommentCount(entityId,entityType) ;
    }


}
