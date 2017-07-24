package com.zengtong.Interceptor;


import com.zengtong.DAO.TicketDao;
import com.zengtong.DAO.UserDao;
import com.zengtong.model.HostHolder;
import com.zengtong.model.Ticket;
import com.zengtong.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Component
public class LoginInterceptor implements HandlerInterceptor{

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        String ticket = null;

        Cookie[] cookies = httpServletRequest.getCookies();

        if(cookies == null){
            return true;
        }

        for(Cookie cookie : cookies){
            if(cookie.getName().equals("ticket")){
                ticket = cookie.getValue();
                break;
            }
        }
        /*
        * cookie 里有ticket,需要进行检测比对
        * */
        if(ticket != null){

            Ticket tic =  ticketDao.selectByTicket(ticket);
            /*
            * 数据库中存在 ticket.
            * */
            if(tic == null || tic.getExpired().before(new Date()) || tic.getStatus() == 1){
                return true;
            }
            int localid = tic.getUserid();

            User user = userDao.selectById(localid);

            hostHolder.setUser(user);

            return true;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle : " + httpServletRequest.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("afterCompletion");
    }
}
