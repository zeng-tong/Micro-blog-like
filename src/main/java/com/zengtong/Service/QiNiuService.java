package com.zengtong.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.zengtong.Utils.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class QiNiuService {


    private static final String GenerateUpToken(){
        Auth auth = Auth.create(Tool.ACCESSKEY,Tool.SECRETKEY);
        return  auth.uploadToken(Tool.bucket);
    }

    public String upToCloud(MultipartFile file){
        /*

华东 	Zone.zone0()
华北 	Zone.zone1()
华南 	Zone.zone2()
北美 	Zone.zoneNa0()

        */

        JSONObject json = new JSONObject();

        Configuration cfg = new Configuration(Zone.zone2());

        UploadManager uploadManager = new UploadManager(cfg);

        String key = null;


        try{

            Response response = uploadManager.put(file.getInputStream(),key,GenerateUpToken(),null,null);

            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);

            json.put(putRet.hash,"Filename");

        } catch (QiniuException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return json.toJSONString();
    }


    /*public String getImageFromCloud(String filename,
                                    HttpServletResponse response) throws IOException {
        int size = 0;
        try{

            size = StreamUtils.copy(new FileInputStream(Tool.QINIUDOMIN + filename),response.getOutputStream());

        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return String.valueOf(size);
    }*/
}
