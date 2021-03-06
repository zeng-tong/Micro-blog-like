package com.zengtong.Controller;

import com.alibaba.fastjson.JSONObject;
import com.zengtong.Service.ImageService;
import com.zengtong.Utils.Tool;
import org.apache.commons.lang3.StringUtils;
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
public class UploadsController {
    @Autowired
    private ImageService imageService;

    @RequestMapping(path = {"/uploadImage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(MultipartFile file, @RequestParam(value = "type", defaultValue = "cloud") String type) {
        try {
            String url = StringUtils.equalsIgnoreCase(type, "local") ? imageService.saveImageLocal(file) : imageService.upToCloud(file);
            System.out.println(url);
            if (url != null) {
                JSONObject ret = Tool.GetJSON(true);
                ret.put("url", url);
                return ret.toJSONString();
            }
        } catch (Exception e) {
            return Tool.getJSONString(false);
        }
        return Tool.GetJSONString(false);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upImage(@RequestParam("file") MultipartFile[] files){
        Map<String,Object> map = new HashMap<>();

        for(MultipartFile file:files){

            String url = imageService.saveImageLocal(file);

            if(url == null){

                map.put(file.getOriginalFilename(),  "上传失败"); //

            }
            map.put(url,"上传成功");
        }

        return Tool.getJSONString(0,map);

    }

    @RequestMapping(value = "/qiniu", method = RequestMethod.POST)
    @ResponseBody
    public String upImageToQiNiu(@RequestParam("file") MultipartFile[] files){

        Map<String,Object> map = new HashMap<>();

        for(MultipartFile file:files){

            String filename = imageService.upToCloud(file);

            if(filename == null){

                map.put(file.getOriginalFilename() , "上传失败");

            }
            map.put(filename,"上传成功");
        }

        return Tool.getJSONString(0,map);

    }



    @RequestMapping(value = "/image",method = RequestMethod.GET)
    @ResponseBody
    public String getImage(@RequestParam("name")String name,
                           HttpServletResponse response) {
        Map<String ,Object> map = new HashMap<>();

        try {

            String statu = imageService.getImageLocal(name,response);

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

    @RequestMapping(value = "/CloudImage",method = RequestMethod.GET)
//    @ResponseBody
    public String GetImageFromQiNiu(@RequestParam("name")String name,
                           HttpServletResponse response) throws IOException {

        Map<String ,Object> map = new HashMap<>();


       /* try {

            String statu = qiNiuService.getImageFromCloud(name,response);

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
        }*/

       return "redirect:" + Tool.QINIUDOMIN + name;
    }


}
