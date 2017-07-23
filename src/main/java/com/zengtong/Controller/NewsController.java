package com.zengtong.Controller;

import com.zengtong.Service.NewsService;
import com.zengtong.Utils.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class NewsController {

    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upImage(@RequestParam("file") MultipartFile file){
        Map<String,Object> map = new HashMap<>();

        String url = newsService.uploadImage(file);

        if(url == null){
            map.put("error","上传失败");
            return Tool.getJSONString(1,map);
        }

        map.put("success",url);
        return Tool.getJSONString(0,map);

    }

    @RequestMapping(value = "/image",method = RequestMethod.GET)
    @ResponseBody
    public String getImage(@RequestParam("name")String name,
                           HttpServletResponse response) throws IOException {
        Map<String ,Object> map = new HashMap<>();

        try {

            String statu = newsService.getImage(name,response);

            if(statu == null){
                map.put("error","图片下载失败");
                return Tool.getJSONString(1,map);
            }

            map.put("success","下载成功");

            return Tool.getJSONString(0,map); //
        }
        catch (Exception e){
            e.printStackTrace();
            map.put("error",e.getMessage());
            return Tool.getJSONString(1,map);
        }
    }

}