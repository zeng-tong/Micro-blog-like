package com.zengtong.Controller;


import com.alibaba.fastjson.JSONObject;
import com.zengtong.DAO.TicketDao;
import com.zengtong.Service.UserSercvice;
import com.zengtong.Utils.Tool;
import com.zengtong.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserSercvice userSercvice;

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = "/register",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String register(@RequestParam("username")String username,
                           @RequestParam("password")String password){


        Map<String,Object> map = userSercvice.register(username,password);

        if(map.containsKey("error")){
            return Tool.getJSONString(1,map.get("error").toString());
        }

        return Tool.getJSONString(0,"注册成功");
    }


    @RequestMapping(value = "/login",method = {RequestMethod.GET,RequestMethod.POST} )
    @ResponseBody
    public String login(HttpServletResponse response,
                        @RequestParam("name")String name,
                        @RequestParam("password")String pwd) throws ServletException, IOException {


        JSONObject json = new JSONObject();
        /*
        * 如果检测拦截器检测到用户登录状态是有效的 ,直接return ,不再重复进行的登录.
        * */
        if(hostHolder.getUser() != null){
            json.put("msg","User :" + hostHolder.getUser().getName() + "already logged on.");
            return json.toJSONString();
        }

        Map map = userSercvice.login(name,pwd);

        if(map.containsKey("error")){
            json.put("error",map.get("error"));
        }

        try{
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setMaxAge(1000 * 3600);
                cookie.setPath("/");
                response.addCookie(cookie);
                map.put("msg","Login success,welcome " + name);
                json.putAll(map);
                return json.toJSONString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return json.toJSONString();
    }
    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();

        String ticket = null;

        for(Cookie cookie : cookies){
            if(cookie.getName().equals("ticket")){
                ticket = cookie.getValue();
            }
        }
        if(ticket != null){
            ticketDao.updateStatus(ticket,1);
        }
        hostHolder.clear();
        return "redirect:/";
    }
}
