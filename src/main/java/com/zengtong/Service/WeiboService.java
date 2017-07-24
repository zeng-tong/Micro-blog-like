package com.zengtong.Service;


import com.zengtong.DAO.WeiboDao;
import com.zengtong.model.Weibo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class WeiboService {

    @Autowired
    private WeiboDao weiboDao;

    @Autowired
    private QiNiuService qiNiuService;

    public String UpWeibo(int user_id,String content, MultipartFile[] files){

        StringBuilder pic_url = new StringBuilder();

        for(MultipartFile file : files){

           pic_url.append(qiNiuService.upToCloud(file) + "|");

        }

        Weibo weibo = new Weibo();
        weibo.setStatus(0);
        weibo.setCommentCount(0);
        weibo.setLikeCount(0);
        weibo.setUserId(user_id);
        weibo.setCreateDate(new Date());
        weibo.setPicUrl(pic_url.toString());
        weibo.setContent(content);


        weiboDao.insertWeibo(weibo);

        return pic_url.toString();
    }

}
