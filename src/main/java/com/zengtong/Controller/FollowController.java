package com.zengtong.Controller;

import com.zengtong.Async.EventModel;
import com.zengtong.Async.EventProducer;
import com.zengtong.Async.EventType;
import com.zengtong.Service.FollowService;
import com.zengtong.Utils.Tool;
import com.zengtong.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController {


    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FollowService followService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(value = "/follow",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String follow(@RequestParam("userId")int userId){

        if (hostHolder.getUser() == null) {
            return Tool.getJSONString(999,"用户未登陆");
        }

        int myId = hostHolder.getUser().getId();

        if (followService.follow(myId,userId) == null){
            return Tool.getJSONString(1,"不要重复关注");
        }

        eventProducer.fireEvent(new EventModel().setEventType(EventType.MESSAGE).setTo_id(userId).setFrom_id(myId));

        return Tool.getJSONString(0,"关注成功");

    }
}
