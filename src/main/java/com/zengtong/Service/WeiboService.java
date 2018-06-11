package com.zengtong.Service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zengtong.DAO.UserDao;
import com.zengtong.DAO.WeiboDao;
import com.zengtong.Utils.JedisAdaptor;
import com.zengtong.Utils.RedisKeyUtil;
import com.zengtong.Utils.Tool;
import com.zengtong.model.Weibo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class WeiboService {

    @Autowired
    private WeiboDao weiboDao;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JedisAdaptor jedisAdaptor;

    public int UpWeibo(int user_id,String content, String images){

        if (StringUtils.isBlank(content)){
            return 0;
        }

        Weibo weibo = new Weibo();
        weibo.setStatus(0);
        weibo.setCommentCount(0);
        weibo.setLikeCount(0);
        weibo.setUserId(user_id);
        weibo.setCreatedDate(new Date());
        weibo.setPicUrl(images);
        weibo.setContent(content);


        weiboDao.insertWeibo(weibo);

        return weibo.getId();
    }


    public String getFeedAsString(int loginID,int offset,int limit){

        String key = RedisKeyUtil.getBizFollowlistKey(loginID);

        Set<String> userIDs = jedisAdaptor.zrange(key,offset,limit);

        JSONArray jsonArray = new JSONArray();

        //如果关注列表为空，或者关注的人没有微博记录.

        if (userIDs == null || userIDs.isEmpty()){

            List<Weibo> list = weiboDao.selectByfavor(offset,limit);

            for (Weibo weibo : list){

                JSONObject json = new JSONObject();

                json.put("Name: ",userDao.selectById(Integer.valueOf(weibo.getUserId())).getName());

                json.put("CreateDate: ",weibo.getCreatedDate());

                json.put("content: ",weibo.getContent());

                json.put("PicURL: ",weibo.getPicUrl());

                json.put("LikeCount: ",weibo.getLikeCount());

                jsonArray.add(json);

            }

            return jsonArray.toJSONString();

        }

        //

        for (String userID : userIDs){

            JSONObject json = new JSONObject();

            Weibo weibo = weiboDao.selectWeiboById(Integer.valueOf(userID));

            json.put("Name: ",userDao.selectById(Integer.valueOf(userID)).getName());

            json.put("CreateDate: ",weibo.getCreatedDate());

            json.put("content: ",weibo.getContent());

            json.put("PicURL: ",weibo.getPicUrl());

            json.put("LikeCount: ",weibo.getLikeCount());


            jsonArray.add(json);
        }

        return jsonArray.toJSONString();
    }



    public List<Weibo> getFeed(int loginID,int offset,int limit){

        String key = RedisKeyUtil.getBizFollowlistKey(loginID);

        Set<String> userIDs = jedisAdaptor.zrange(key,offset,limit);

        List<Weibo> list = new LinkedList<>();

        for (String userID : userIDs){
            list.add(weiboDao.selectOneByUserID(Integer.valueOf(userID)));
        }
        //To do ：　Feed流的显示顺序策略.

        //如果关注列表为空，或者关注的人没有微博记录.

//        if (userIDs == null || userIDs.isEmpty()){

             list = weiboDao.selectByfavor(offset,limit);

//            return addImageDomain(list);
//        }

//        List<Weibo> list = new LinkedList<>();

        return addImageDomain(list);

    }



    public List<Weibo> ListWeiboByUserId(int usrId,int offset,int count){

        List<Weibo> weibos =  weiboDao.showWeiboByUserId(usrId,offset,count);

        if( weibos.isEmpty()) return null;

        return addImageDomain(weibos);

      /*  JSONArray jsonArray = new JSONArray();

        User user = userDao.selectById(usrId);*/

        /*for(Weibo weibo : weibos){
            JSONObject json = new JSONObject();
            json.put("username",user.getName());
            json.put("CommentCount",weibo.getCommentCount());
            json.put("LikeCount",weibo.getLikeCount());
            json.put("CreateDate",weibo.getCreatedDate());
            json.put("Content",weibo.getContent());
            json.put("pic_url",splitPicName(weibo.getPicUrl()));
            jsonArray.add(json);
        }
        return jsonArray.toJSONString();*/
    }

    private List<Weibo> addImageDomain(List<Weibo> weibos){

        for (Weibo weibo : weibos){

            String image = weibo.getPicUrl();

            if (image == null){
                continue;
            }

            String[] images = StringUtils.split(image,"|");

            StringBuilder res = new StringBuilder();

            for (String str : images){
                res.append(Tool.QINIUDOMIN + str + "|");
            }

            weibo.setPicUrl( res.toString());
        }

        return weibos;

    }

    public List<Weibo> ListAllWeibo(int offset,int count){

        List<Weibo> weibos = weiboDao.showAllWeibo(offset,count);

        if(weibos.isEmpty()) return  null;

        return addImageDomain(weibos);

       /* JSONArray jsonArray = new JSONArray();

        for (Weibo weibo : weibos){
            User user =  userDao.selectById(weibo.getUserId());
            JSONObject json = new JSONObject();
            json.put("username",user.getName());
            json.put("CommentCount",weibo.getCommentCount());
            json.put("LikeCount",weibo.getLikeCount());
            json.put("CreateDate",weibo.getCreatedDate());
            json.put("Content",weibo.getContent());
            json.put("pic_url",splitPicName(weibo.getPicUrl()));
            jsonArray.add(json);
        }
        return jsonArray.toJSONString();*/
    }

    public String deleteWeibo(int weiboId,int userId){


        Weibo weibo = weiboDao.selectWeiboById(weiboId);

        if(weibo == null ){
            return Tool.getJSONString(1,"这条微博不存在");
        }
        if(userId == weibo.getUserId()){
            weiboDao.deleteWeibo(weiboId);
            return Tool.getJSONString(0,"删除成功");
        }

        return Tool.getJSONString(1,"怎么能让你删除别人的微博呢?!");
    }

}
