package com.zengtong.Controller;

import com.sun.net.httpserver.HttpServer;
import com.zengtong.DAO.TestDao;
import com.zengtong.DAO.UserDao;
import com.zengtong.Service.UserSercvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.*;
import java.util.Enumeration;


/**
 * Created by znt on 17-7-18.
 */
@Controller
public class homeController {



    /**
     * thymleaf
     * */
    @GetMapping(value = "/")
    public String home(Model model,
                       HttpSession session){
        model.addAttribute("name","ZengTong");
        model.addAttribute("msg", session.getAttribute("msg"));
        return "home.html";
    }

    @Autowired
    private TestDao testDao;

    @GetMapping(value = "/dao")
    @ResponseBody
    public String dao(){
        return testDao.say();
    }

    @GetMapping(value = "/request")
    @ResponseBody
    public String setting(HttpServletRequest request) {

        StringBuilder stringBuilder = new StringBuilder();

        Enumeration enumeration = request.getHeaderNames();

        while(enumeration.hasMoreElements()){
            String headName = enumeration.nextElement().toString();
            stringBuilder.append(headName +" : "+ request.getHeader(headName) + "<br>");
        }

        for(Cookie cookie : request.getCookies()){
            stringBuilder.append("Cookie: "+ cookie.getName() +":" + cookie.getValue() + "<br>");
        }

        return stringBuilder.toString();

    }

    @GetMapping(value = "/response")
    @ResponseBody
    public String response(@CookieValue(value = "myID",defaultValue = "12")String myID,
                           @RequestParam(value = "key")String key,
                           @RequestParam(value = "value")String value,
                           HttpServletResponse response){
        response.addCookie(new Cookie(key,value));
        return "my Cookie :" + myID + "<br>";
    }

    @RequestMapping(value = "/redirect/{code}")
    public String redirect(@PathVariable("code") int code,
                                 HttpSession httpSession){

    /*    RedirectView red = new RedirectView("/",true);

        if(code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }*/

        httpSession.setAttribute("msg","Jump from redirect");

        return "redirect:/";

    }

    @RequestMapping(value = "/admin/{code}")
    @ResponseBody
    public String admin(@PathVariable("code") int code) throws Exception {
        if(code == 1) return "hello";
        throw new  Exception("No privilege");
    }


    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "error: " + e.getMessage();
    }


    @Autowired
    private UserSercvice userSercvice;

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestParam("Username")String username,
                           @RequestParam("Age")Integer age,
                           @RequestParam("Password")String password){
        userSercvice.update(username,age,password);
        return "Update success . ";
    }



}
