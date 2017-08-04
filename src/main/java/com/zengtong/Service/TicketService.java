package com.zengtong.Service;

import com.zengtong.DAO.TicketDao;
import com.zengtong.model.Ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TicketService {

    @Autowired
    TicketDao ticketDao;

    public Map<String,String> GetTicket(String Str_ticket){

        Ticket  ticket = ticketDao.selectByTicket(Str_ticket);

        Map map = new HashMap<String,Object>();

        if(ticket == null){
            map.put("error",null);
            return map;
        }

        map.put("ticket",ticket.getTicket());

        return map;
    }

    public String  addTicket(int userid) {

        Ticket ticket = new Ticket();


        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24); //  计算机中时间以毫秒存储,1000毫秒为1秒。所以设置的有效期为1天

        ticket.setUserid(userid);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        ticket.setStatus(0);
        ticket.setExpired(date);
        ticketDao.addTicket(ticket);

        return ticket.getTicket();
    }

}
