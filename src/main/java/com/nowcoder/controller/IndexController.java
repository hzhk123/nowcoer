package com.nowcoder.controller;


import com.nowcoder.model.User;
import com.nowcoder.service.ToutiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/*

 */
@Controller
public class IndexController {

    @Autowired
    private ToutiaoService toutiaoService ;
    //@RequestMapping("/index")
    @ResponseBody
    public String index(HttpSession session) {
            return "hello nowcoder"+"<br> Say:"+toutiaoService.say();
    }
    @RequestMapping(path={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type",defaultValue = "1") int type,
                          @RequestParam(value = "key",defaultValue = "nowcoder") String key)
    {
        return String.format("GID{%s},UID{%d},TYPE{%d}.KEY{%s}",groupId,userId,type,key) ;
    }

    @RequestMapping(path = {"/vm"})
    public String news(Model model)
    {
        model.addAttribute("value","test");
        List<String> colors= Arrays.asList(new String[] {"blue","rede","green"});

        Map<String,String> map = new HashMap<String,String>();
        for (int i=0; i<4; i++)
        {
            map.put(String.valueOf(i),String.valueOf(i*i));
        }

        model.addAttribute("colors",colors);
        model.addAttribute("map",map);
        model.addAttribute("user",new User("Jim")) ;
        return  "news" ;
    }

    @RequestMapping("/request")
    @ResponseBody
    public  String request(HttpServletRequest request,
                           HttpServletResponse  response,
                           HttpSession session)
    {
        StringBuilder sb = new StringBuilder() ;

        Enumeration<String> headnames = request.getHeaderNames() ;
        while (headnames.hasMoreElements())
        {
            String name = headnames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>") ;
        }

        for (Cookie cookie :request.getCookies() )
        {
            sb.append("Cookie:") ;
            sb.append(cookie.getName()) ;
            sb.append(":");
            sb.append(cookie.getValue()) ;
            sb.append("<br>") ;
        }
        return  sb.toString();
    }

    @RequestMapping("/response")
    @ResponseBody
    public  String response(@CookieValue(value="nowcoderid", defaultValue = "id") String nowcoderid ,
                            @RequestParam(value = "key", defaultValue = "key") String key,
                            @RequestParam(value = "value", defaultValue = "value") String value,
                            HttpServletResponse response){
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);

        return "NowcoderId:"+nowcoderid ;

    }

    @RequestMapping("/redirect/{code}")
    @ResponseBody
    public String redirect(@PathVariable("code") int code,
                           HttpSession session){

//        if (code == 301)
//        {
//            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
//        }
        session.setAttribute("msg","jump from redirect");
        return  "/redirect:/" ;
    }

    @RequestMapping("/admin")
    @ResponseBody
    public  String admin(@RequestParam(value = "key",required = false) String key){
        if ("admin".equals(key))
        {
            return  "hello admin" ;
        }
        throw new IllegalArgumentException("key cuowu") ;
    }

    @ExceptionHandler
    @ResponseBody
    public  String erro(Exception e)
    {
        return "erro:"+e.getMessage() ;
    }

}
