package com.nowcoder.controller;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Controller
public class LoginController {
    private  static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value ="rember",defaultValue = "0") int remember,
                      HttpServletResponse response) {

        Map<String, Object> map = userService.register(username,password) ;
        try {
              /*
            ***********************************注册成功，用户获得一个ticket，此时map不为空************************
             */
            if (map.containsKey("ticket"))
            {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString()) ;
                cookie.setPath("/");
                cookie.setMaxAge(3600*24*5);
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0,map);
            }else {
                return ToutiaoUtil.getJSONString(1,map) ;
            }

        }catch (Exception e)
        {
            logger.error("注册异常"+e.getMessage());
            return  ToutiaoUtil.getJSONString(1,map) ;

        }
    }


    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value ="rember",defaultValue = "0") int remember) {
        try {
            Map<String, Object> map = userService.login(username,password) ;
            /*
            ***********************************登录成功，用户获得一个ticket，此时map不为空************************
             */
            if (map.containsKey("ticket"))
            {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString()) ;
                cookie.setPath("/");
                cookie.setMaxAge(3600*24*5);
                return ToutiaoUtil.getJSONString(0,map);
            }else {
                return ToutiaoUtil.getJSONString(1,map) ;
            }

        }catch (Exception e)
        {
            logger.error("登陆异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"登陆异常") ;
        }
    }
    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket) ;
        return "redirect:/" ;
    }



}
