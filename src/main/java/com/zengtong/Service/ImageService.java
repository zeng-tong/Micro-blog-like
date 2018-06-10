package com.zengtong.Service;

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
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageService {


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
        String filename;

        Configuration cfg = new Configuration(Zone.zone2());

        UploadManager uploadManager = new UploadManager(cfg);

        String key = null;

        try{

            Response response = uploadManager.put(file.getInputStream(),key,GenerateUpToken(),null,null);

            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);

            filename = putRet.hash;

        } catch (QiniuException e) {
            e.printStackTrace();
            filename = null;
        } catch (IOException e) {
            e.printStackTrace();
            filename = null;
        }

        return filename;
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
    public String saveImageLocal(MultipartFile file) {


        if (!Tool.isAllowUpload(file.getOriginalFilename())){
            return null;
        }

        final String filename = UUID.randomUUID().toString().replaceAll("-","")+"."+ Tool.suffix(file.getOriginalFilename());


        try {
            StreamUtils.copy(file.getBytes(),new FileOutputStream(Tool.IMAGE_DIR + filename));
            return Tool.MYDOMIN + "image?name=" + filename;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public String getImageLocal(String filename, HttpServletResponse response) throws IOException {



        response.setContentType("image/" + Tool.suffix(filename));

        try{

            StreamUtils.copy(new FileInputStream(Tool.IMAGE_DIR + filename),response.getOutputStream());

            return filename;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
