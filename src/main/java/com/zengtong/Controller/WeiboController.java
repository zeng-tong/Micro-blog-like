package com.zengtong.Controller;

import com.zengtong.Async.EventProducer;
import com.zengtong.Service.WeiboService;
import com.zengtong.Utils.Tool;
import com.zengtong.model.HostHolder;
import com.zengtong.model.Weibo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Controller
public class WeiboController {

    @Autowired
    private WeiboService weiboService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

/*    @RequestMapping(value = "/weibo",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String UpWeibo(@RequestParam("content")String content,
                          @RequestParam(value = "pic",required = false)MultipartFile[] files, // required = false 没有这个参数的时候不会报404
                          HttpServletResponse response) throws IOException {

        if(hostHolder.getUser() == null){
            *//*
            * 判断用户是否为登录状态,未登录则跳回首页
            * *//*
            response.sendRedirect("/");
            return Tool.getJSONString(1,"登录之后才能上传");
        }

        if(content == null){
            return Tool.getJSONString(1,"文本内容不能为空");
        }

        int WeiBoID = weiboService.UpWeibo(hostHolder.getUser().getId(),content,files);



        eventProducer.fireEvent(new EventModel().setUser_id(hostHolder.getUser().getId()).setEntity_type(0).setEntity_id(WeiBoID).setEventType(EventType.FEEDCENTER));

        return Tool.getJSONString(0,"WeiboID: " + String.valueOf(WeiBoID));
    }*/

    @RequestMapping(value = "/showWeibo")
    @ResponseBody
    public String showWeibo(@RequestParam(value = "userId",defaultValue = "")String usrId){

        List<Weibo> res;

        if (usrId.equals("")) {
            res = weiboService.ListAllWeibo(0, 10);
        } else {
            res = weiboService.ListWeiboByUserId(Integer.valueOf(usrId), 0, 10);
        }

        if(res == null) return Tool.getJSONString(1,"没有该用户的记录");

        return Tool.getJSONString(0,res.toString());

    }

    @RequestMapping(value = "/feedWeibo")
    @ResponseBody
    public String feedWeibo(){


        if (hostHolder.getUser() == null){
            return Tool.getJSONString(999,"用户未登录");
        }

       return weiboService.getFeedAsString(hostHolder.getUser().getId(),0,10);


    }

    @RequestMapping(value = "/deleteWeibo")
    @ResponseBody
    public String deleteWeibo(@RequestParam("weiboId") int weiboId,
                              HttpServletResponse response) throws IOException {

        if(hostHolder.getUser() == null){
            response.sendRedirect("/");
            return Tool.getJSONString(1,"登录之后才能删除");
        }

        int userid = hostHolder.getUser().getId();

        return weiboService.deleteWeibo(weiboId,userid);

    }

}
