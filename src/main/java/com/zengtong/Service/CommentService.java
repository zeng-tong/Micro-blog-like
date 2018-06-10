package com.zengtong.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zengtong.DAO.CommentDao;
import com.zengtong.DAO.UserDao;
import com.zengtong.DAO.WeiboDao;
import com.zengtong.Utils.Tool;
import com.zengtong.model.Comment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service

public class CommentService {


    private static final int WEIBO_COMMENT = 0;

    private static final int COMMENT_COMMENT = 1;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private WeiboDao weiboDao;


    @Autowired
    private ImageService imageService;


    public int addComment(Comment comment) {

        if (StringUtils.isBlank(comment.getContent())) {
            return 0;
        }
        int addedCount = commentDao.addComment(comment);
        System.out.println("addedCount: " + addedCount);
        if (addedCount == 0) {
            return addedCount;
        } else {
            switch (comment.getEntityType()) {
                case WEIBO_COMMENT:
                    weiboDao.addCommentCount(comment.getEntityId());
                    break;
                case COMMENT_COMMENT:
                    commentDao.addCommentCount(comment.getEntityId());
                    break;
            }
        }
        return addedCount;
    }


    public String addComment(int entityType, int entityId, int userId,
                             MultipartFile[] files, String content) {
        /*
         * entityType : 0,微博评论 ; 1,评论的评论
         * */
        String picUrl = null;

        if (files != null) {

            for (MultipartFile file : files) {

                picUrl = imageService.upToCloud(file) + "|";

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

        switch (entityType) {
            case WEIBO_COMMENT:
                weiboDao.addCommentCount(entityId);
                break;
            case COMMENT_COMMENT:
                commentDao.addCommentCount(entityId);
                break;
        }
        return Tool.getJSONString(0, "评论成功");
    }

    @Autowired
    private UserDao userDao;

    public String showComment(int entityType, int entityId, int offset, int count) {


        JSONArray jsonArray = new JSONArray();


        List<Comment> comments = commentDao.showComment(entityType, entityId, offset, count);

        if (comments.isEmpty()) {
            return null;
        } else {
            for (Comment comment : comments) {

                JSONObject json = new JSONObject();

                String username = userDao.selectById(comment.getUserId()).getName();

                json.put("NickName", username);

                json.put("createDate", comment.getCreateDate());

                json.put("content", comment.getContent());

                json.put("picUrl", Tool.splitPicName(comment.getPicUrl()));

                json.put("likeCount", comment.getLikeCount());

//                comment.getReplyCount()
                jsonArray.add(json);

            }

        }

        return jsonArray.toJSONString();
    }


    public String deleteComment(int commentId, int userId) {

        Comment comment = commentDao.selectCommentById(commentId);

        if (comment == null) {
            return Tool.getJSONString(1, "没有评论");
        } else if (userId == comment.getUserId()) {

            switch (comment.getEntityType()) {
                case WEIBO_COMMENT:
                    weiboDao.minusCommentCount(commentDao.selectCommentById(commentId).getEntityId());
                    break;
                case COMMENT_COMMENT:
                    commentDao.minusCommentCount(commentDao.selectCommentById(commentId).getEntityId());
            }
            commentDao.deleteComment(commentId);
            return Tool.getJSONString(0, "删除成功");
        }

        return Tool.getJSONString(1, "怎么能让你删除别人的评论呢?!");
    }
}
