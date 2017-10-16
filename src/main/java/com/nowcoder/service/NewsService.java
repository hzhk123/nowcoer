package com.nowcoder.service;

import com.nowcoder.dao.CommentDao;
import com.nowcoder.dao.NewsDAO;
import com.nowcoder.model.News;
import com.nowcoder.util.ToutiaoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;


    public  List<News> getLatestNews(int userId, int offset, int limit)
    {
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit) ;
    }

    public int addNews(News news){
        newsDAO.addNews(news) ;
        return news.getId();
    }

    public News getById(int newsId){
        return  newsDAO.getById(newsId) ;
    }

    public  String saveImage(MultipartFile file) throws IOException{

        /*
        * 判断文件格式是否符合：后缀
        * */
        int docpos = file.getOriginalFilename().lastIndexOf(".") ;
        if (docpos < 0)
        {
            return  null ;
        }
        /*
        **************************************判断上传文件的扩展名是否符合要求**************************
         */
        String ext = file.getOriginalFilename().substring(docpos+1).toLowerCase();
        if (!ToutiaoUtil.isFillExt(ext)){
            return null;
        }

        String imagename = UUID.randomUUID().toString().replaceAll("-", "") + "." + ext ;
        Files.copy(file.getInputStream(), new java.io.File(ToutiaoUtil.imageFile + imagename).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        //图片保存完成，需要返回给前端进行调用显示
        return ToutiaoUtil.TOUTIAO_DOMAIN + "image/?name=" + imagename ;

    }

    public int updateCommentCount(int id, int count){
        return newsDAO.updateCommentCount(id, count) ;
    }
}
