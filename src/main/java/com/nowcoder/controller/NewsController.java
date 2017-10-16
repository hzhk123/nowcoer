package com.nowcoder.controller;


import com.nowcoder.dao.NewsDAO;
import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {

    @Autowired
    NewsService newsService ;

    @Autowired
    HostHolder hostHolder ;

    @Autowired
    UserService userService ;

    @Autowired
    CommentService commentService ;

    private  static  final Logger logger = LoggerFactory.getLogger(NewsController.class) ;

    @RequestMapping(path = {"/news/{newsId}"}, method = { RequestMethod.GET})
    public  String  newsDetails(@PathVariable("newsId") int newsId, Model model){

        News news = newsService.getById(newsId) ;
        if (news != null){
            //遍历出评论,传递给前端
            List<Comment> comments = commentService.getCommnentByEntity(news.getId(),EntityType.ENTITY_NEWS) ;
            List<ViewObject> commentVOs = new ArrayList<ViewObject>() ;
            for (Comment comment : comments){
                ViewObject commentVO = new ViewObject();
                commentVO.set("comment",comment);
                commentVO.set("user",userService.getUser(comment.getUserId()));
                commentVOs.add(commentVO) ;
            }
            model.addAttribute("comments",commentVOs) ;
        }
        model.addAttribute("news",news) ;
        model.addAttribute("owner", userService.getUser(news.getUserId())) ;
        return "detail";
    }

    @RequestMapping(path = {"user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        try {
            News news = new News() ;
            if (hostHolder.getUser()!=null) {
                news.setUserId(hostHolder.getUser().getId());
            }else {
                news.setUserId(3);
            }
            news.setCreatedDate(new Date());
            news.setImage(image);
            news.setTitle(title);
            news.setLink(link);
            newsService.addNews(news) ;
            return  ToutiaoUtil.getJSONString(0) ;
        }catch (Exception e){
            logger.error("错误" + e.getMessage());
        }
        return ToutiaoUtil.getJSONString(0 ) ;

    }


    @RequestMapping(path = {"/image/"}, method = { RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String  imageName,
                           HttpServletResponse response) {
        try {
            response.setContentType("image/jpg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.TOUTIAO_DOMAIN + imageName)),
                    response.getOutputStream());
        }catch (Exception e){
            logger.error("读取图片失败" + imageName + e.getMessage());
        }
    }


    @RequestMapping(path = {"/uploadImage/"}, method = { RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile  file) {

        try {
            String fileURL = newsService.saveImage(file) ;
            if (fileURL == null){
                return ToutiaoUtil.getJSONString(1,"上传失败") ;
            }
            return ToutiaoUtil.getJSONString(0, fileURL) ;

        }catch (Exception e){
            logger.error("上传图片失败", e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传失败") ;
        }
    }

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String  addComment(@RequestParam("newsId") int newsId,
                           @RequestParam("content") String content){
        try{
            if (hostHolder != null) {
                Comment comment = new Comment();
                comment.setUserId(hostHolder.getUser().getId());
                comment.setContent(content);
                comment.setCreatedDate(new Date());
                comment.setStatus(0);
                comment.setEntityId(newsId);
                comment.setEntityType(EntityType.ENTITY_NEWS);
                commentService.addComment(comment) ;

                int count = commentService.getCommnentCount(comment.getEntityId(),comment.getEntityType()) ;
                newsService.updateCommentCount(comment.getEntityId(), count) ;
            }
        }catch (Exception e){
            logger.error("添加评论失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(0,"添加评论没失败") ;
        }
        return "redirect:/news/"+newsId ;
    }
}
