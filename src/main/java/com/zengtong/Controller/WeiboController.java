package com.zengtong.Controller;

import com.zengtong.Async.EventProducer;
import com.zengtong.Service.WeiboService;
import com.zengtong.Utils.Tool;
import com.zengtong.model.HostHolder;
import com.zengtong.model.Weibo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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


    @RequestMapping(path = {"/weibo"}, method = {RequestMethod.POST})
    @ResponseBody

    public String addWeibo(@RequestParam("content") String content, @RequestParam(value = "images", defaultValue = "") String images) {
        System.out.println("addWeibo: " + images);
        try {
            if (hostHolder.getUser() == null) {
                return Tool.GetJSONString(false, "请先登陆.");
            }
            int wid = weiboService.UpWeibo(hostHolder.getUser().getId(), content, images);

            return Tool.GetJSONString(wid > 0);
        } catch (Exception e) {
            return Tool.GetJSONString(false, "系统异常");
        }
    }

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
