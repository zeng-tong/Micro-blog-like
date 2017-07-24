package com.zengtong.Controller;

import com.zengtong.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


/**
 * Created by znt on 17-7-18.
 */
@Controller
public class homeController {

    @Autowired
    private HostHolder hostHolder;

    /**
     * thymleaf
     * */
    @GetMapping(value = "/")
    public String home(HttpSession session){
    /*    model.addAttribute("name","ZengTong");
        model.addAttribute("user",new User("小哥",10,"pswd"));*/

        String info =  "NULL";

        if(hostHolder.getUser() != null){
            info = hostHolder.getUser().getName();
        }

        session.setAttribute("info",info);


        return "home";

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

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "error: " + e.getMessage();
    }




}
