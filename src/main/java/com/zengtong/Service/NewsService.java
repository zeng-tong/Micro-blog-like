package com.zengtong.Service;


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


    public String getImage(String filename, HttpServletResponse response) throws IOException {

        int index = filename.lastIndexOf('.');

        String suffix = filename.substring(index + 1);

        response.setContentType("image/" + suffix);

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
