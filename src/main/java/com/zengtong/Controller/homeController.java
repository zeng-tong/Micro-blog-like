package com.zengtong.Controller;

import com.zengtong.DAO.TestDao;
import com.zengtong.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    public String home(HttpSession session){
    /*    model.addAttribute("name","ZengTong");
        model.addAttribute("user",new User("小哥",10,"pswd"));*/
    session.setAttribute("user",new User("Zengtong",20,"Hello"));

    return "home";

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




}
