package com.zengtong.Service;


import com.zengtong.Utils.Tool;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.util.UUID;


@Service
public class NewsService {

    public String uploadImage(MultipartFile file) {

        int index = file.getOriginalFilename().lastIndexOf('.');

        String FileType = file.getOriginalFilename().substring(index+1).toLowerCase();

        if (!Tool.isAllowUpload(FileType)){
            return null;
        }

        final String filename = UUID.randomUUID().toString().replaceAll("-","")+"."+FileType;


        try {
            StreamUtils.copy(file.getBytes(),new FileOutputStream(Tool.IMAGE_DIR + filename));
            return Tool.MYDOMIN + "image?name=" + filename;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


   /* public String getImage(String filename, HttpServletResponse response){

    }*/
}
