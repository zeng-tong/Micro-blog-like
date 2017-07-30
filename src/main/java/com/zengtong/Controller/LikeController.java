package com.zengtong.Controller;


import com.zengtong.Service.LikeService;
import com.zengtong.Utils.Tool;
import com.zengtong.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {


    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @RequestMapping(value = "/like",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String like(@RequestParam(value = "entityType")int entityType,
                       @RequestParam(value = "entityId")int entityId){

        if(hostHolder.getUser() == null) return Tool.getJSONString(999,"用户未登陆.");



        long likes = likeService.like(entityId,entityType,hostHolder.getUser().getId());

        if(likes == 0){
            return Tool.getJSONString(1,"点赞失败");
        }



        return Tool.getJSONString(0,String.valueOf(likes));

    }


}
