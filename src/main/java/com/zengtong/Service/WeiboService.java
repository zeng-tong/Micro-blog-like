package com.zengtong.Service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zengtong.DAO.UserDao;
import com.zengtong.DAO.WeiboDao;
import com.zengtong.Utils.Tool;
import com.zengtong.model.User;
import com.zengtong.model.Weibo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

import static com.zengtong.Utils.Tool.splitPicName;

@Service
public class WeiboService {

    @Autowired
    private WeiboDao weiboDao;

    @Autowired
    private QiNiuService qiNiuService;

    @Autowired
    private UserDao userDao;

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



    public String ListWeiboByUserId(int usrId,int offset,int count){

        List<Weibo> weibos =  weiboDao.showWeiboByUserId(usrId,offset,count);

        if( weibos.isEmpty()) return null;

        JSONArray jsonArray = new JSONArray();

        User user = userDao.selectById(usrId);

        for(Weibo weibo : weibos){
            JSONObject json = new JSONObject();
            json.put("username",user.getName());
            json.put("CommentCount",weibo.getCommentCount());
            json.put("LikeCount",weibo.getLikeCount());
            json.put("CreateDate",weibo.getCreateDate());
            json.put("Content",weibo.getContent());
            json.put("pic_url",splitPicName(weibo.getPicUrl()));
            jsonArray.add(json);
        }
        return jsonArray.toJSONString();
    }

    public String ListAllWeibo(int offset,int count){

        List<Weibo> weibos = weiboDao.showAllWeibo(offset,count);

        if(weibos.isEmpty()) return  null;

        JSONArray jsonArray = new JSONArray();

        for (Weibo weibo : weibos){
            User user =  userDao.selectById(weibo.getUserId());
            JSONObject json = new JSONObject();
            json.put("username",user.getName());
            json.put("CommentCount",weibo.getCommentCount());
            json.put("LikeCount",weibo.getLikeCount());
            json.put("CreateDate",weibo.getCreateDate());
            json.put("Content",weibo.getContent());
            json.put("pic_url",splitPicName(weibo.getPicUrl()));
            jsonArray.add(json);
        }
        return jsonArray.toJSONString();
    }

    public String deleteWeibo(int weiboId){


        if(weiboDao.selectWeiboById(weiboId) != null){
            weiboDao.deleteWeibo(weiboId);
            return Tool.getJSONString(0,"删除成功");
        }
        return Tool.getJSONString(1,"这条微博不存在");
    }

}
