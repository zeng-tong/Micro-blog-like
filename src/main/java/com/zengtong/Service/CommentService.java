package com.zengtong.Service;

import com.zengtong.DAO.CommentDao;
import com.zengtong.DAO.WeiboDao;
import com.zengtong.Utils.Tool;
import com.zengtong.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service

public class CommentService {


    private static final int ENTITY_WEIBO = 0;

    private static final int ENTITY_COMMENT = 1;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private WeiboDao weiboDao;



    @Autowired
    private QiNiuService qiNiuService;
    public String addComment(int entityType, int entityId, int userId,
                             MultipartFile[] files,String content){
        /*
        * entityType : 0,微博评论 ; 1,评论的评论
        * */
        String picUrl = null;

        if(files != null) {

            for(MultipartFile file : files){

                picUrl = qiNiuService.upToCloud(file) + "|";

            }
        }

        Comment comment = new Comment();

        comment.setContent(content);
        comment.setCreateDate(new Date());
        comment.setEntityId(entityId);
        comment.setEntityType(entityType);
        comment.setLikeCount(0);
        comment.setPicUrl(picUrl);
        comment.setReplyCount(0);
        comment.setStatus(0);
        comment.setUserId(userId);

        commentDao.addComment(comment);

        switch (entityType){
            case ENTITY_WEIBO : weiboDao.addCommentCount(entityId); break;
            case ENTITY_COMMENT : commentDao.addCommentCount(entityId);break;
        }
        return Tool.getJSONString(0,"评论成功");
    }

/*    public String showComment(int entityType,int entityId){


        List<Comment> comments = commentDao.showComment(entityType,entityId);

        if(comments == null){
            return Tool.getJSONString(1,"没有评论");
        }

    }*/
}
