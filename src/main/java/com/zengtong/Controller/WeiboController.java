package com.zengtong.Controller;

import com.zengtong.Service.WeiboService;
import com.zengtong.Utils.Tool;
import com.zengtong.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class WeiboController {

    @Autowired
    private WeiboService weiboService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = "/weibo",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String UpWeibo(@RequestParam("content")String content,
                          @RequestParam("pic")MultipartFile[] files,
                          HttpServletResponse response) throws IOException {

        if(hostHolder.getUser() == null){
            /*
            * 判断用户是否为登录状态,未登录则跳回首页
            * */
            response.sendRedirect("/");
            return Tool.getJSONString(1,"登录之后才能上传");
        }

        if(content == null){
            return Tool.getJSONString(1,"文本内容不能为空");
        }

        weiboService.UpWeibo(hostHolder.getUser().getId(),content,files);

        return Tool.getJSONString(0,"上传成功");
    }

}
