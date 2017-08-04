package com.zengtong.Controller;


import com.zengtong.Async.EventModel;
import com.zengtong.Async.EventProducer;
import com.zengtong.Async.EventType;
import com.zengtong.DAO.TicketDao;
import com.zengtong.DAO.UserDao;
import com.zengtong.Service.UserSercvice;
import com.zengtong.Utils.Tool;
import com.zengtong.model.HostHolder;
import com.zengtong.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserSercvice userSercvice;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/regPage"}, method = {RequestMethod.GET})
    public String regPage() {
        if (hostHolder.getUser() != null) {
            return "redirect:/index";
        }
        return "register";
    }


    @RequestMapping(value = "/register",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String register(@RequestParam("name")String username,
                           @RequestParam("password")String password,
                           @RequestParam("email")String email,
                           HttpServletResponse response){

        Map<String ,Object> res = new HashMap<>();

        try{

            boolean ret = userSercvice.register(username,password,email,res);

            if (ret){

                Tool.UpdateCookieTicket(response,res.get("ticket").toString(),3600 * 24 * 10);

                User user = userDao.selectByEmail(email);

                int user_id = user.getId();

                eventProducer.fireEvent(new EventModel().setTo_id(user_id).setEventType(EventType.REGISTER));

                return Tool.GetJSONString(true);
            }else{
                return Tool.GetJSONString(ret,res);
            }
        }catch (Exception e){
            logger.info(e.getMessage());
            return Tool.GetJSONString(false,res.get("error").toString());
        }

    }

    @RequestMapping(path = {"/loginPage"}, method = {RequestMethod.GET})
    public String loginPage() {
        if (hostHolder.getUser() != null) {
            return "redirect:/index";
        }
        return "login";
    }

    @RequestMapping(value = "/login",method = {RequestMethod.GET,RequestMethod.POST} )
    @ResponseBody
    public String login(HttpServletResponse response,
                        @RequestParam("email")String email,
                        @RequestParam("password")String pwd) throws ServletException, IOException {

        try{

            Map<String ,Object> res = new HashMap<>();

            boolean ret = userSercvice.login(email,pwd,res);

            if (ret){
                Tool.UpdateCookieTicket(response,res.get("ticket").toString(),3600 * 24 * 10);
                return Tool.GetJSON(true).toJSONString();
            }
            else {
                return Tool.GetJSON(ret,res).toJSONString();
            }
        }catch (Exception e){
            logger.info(e.getMessage());
            return Tool.GetJSONString(false,"登陆失败");
        }
    }


    @RequestMapping(value = "/logout")
    public String logout(@CookieValue("ticket")String ticket,
                         HttpServletResponse response){

        if(ticket != null){
            ticketDao.updateStatus(ticket,1);
        }

        hostHolder.clear();

        Tool.UpdateCookieTicket(response,null,0);
        return "redirect:/";
    }


}
