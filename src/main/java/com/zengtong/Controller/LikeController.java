package com.zengtong.Controller;


import com.alibaba.fastjson.JSONObject;
import com.zengtong.Service.LikeService;
import com.zengtong.Utils.Tool;
import com.zengtong.model.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    private static Logger logger = LoggerFactory.getLogger(LikeController.class);



    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @RequestMapping(value = "/like",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String like(@RequestParam(value = "entityType")int entityType,
                       @RequestParam(value = "entityId")int entityId){

        if(hostHolder.getUser() == null)
            return Tool.GetJSONString(false,"用户未登陆.");


        try {

            Double likes = likeService.like(entityId,entityType,hostHolder.getUser().getId());

    /*        if(likes == 0){
                return Tool.GetJSONString(false,"点赞失败");
            }*/

            JSONObject ret = Tool.GetJSON(true);

            ret.put("count",likes);

            return ret.toJSONString();
//            return Tool.getJSONString(0,String.valueOf(likes));

        }catch (Exception e){
            logger.error(e.getMessage());


            String ret =  Tool.GetJSONString(false,"喜欢系统异常");

            return Tool.GetJSONString(false,"喜欢系统异常");
        }


    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("entityType") int entityType, @RequestParam("entityId") int entityId) {

        if (hostHolder.getUser() == null) {
            return Tool.GetJSONString(false, "未登陆");
        }

        try {

            Double likeCount = likeService.dislike(hostHolder.getUser().getId(), entityType, entityId);

            JSONObject ret = Tool.GetJSON(true);

            ret.put("count", likeCount);

            return ret.toJSONString();

        } catch (Exception e){
            return  Tool.GetJSONString(false, "取消喜欢异常");
        }
    }


}
