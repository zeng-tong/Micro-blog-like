package com.zengtong.Controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zengtong.DAO.CommentDao;
import com.zengtong.DAO.UserDao;
import com.zengtong.Service.CommentService;
import com.zengtong.Service.LikeService;
import com.zengtong.Utils.Tool;
import com.zengtong.model.Comment;
import com.zengtong.model.EntityType;
import com.zengtong.model.HostHolder;
import com.zengtong.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
public class CommentController {

    private static Logger logger = LoggerFactory.getLogger(CommentController.class);


    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserDao userDao;

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam("content") String content, @RequestParam("entityType") int entityType,
                             @RequestParam("entityId") int entityId) {
        if (hostHolder.getUser() == null) {
            return Tool.GetJSONString(false, "未登陆");
        }

        try {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setEntityId(entityId);
            comment.setEntityType(entityType);
            comment.setUserId(hostHolder.getUser().getId());
            comment.setCreateDate(new Date());
            int cid = commentService.addComment(comment);
            return Tool.GetJSONString(cid > 0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Tool.GetJSONString(false, "系统异常");
        }
    }


    @Autowired
    private CommentDao commentDao;
    @RequestMapping(path = {"/listComments"}, method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listComments(@RequestParam("entityType") int entityType, @RequestParam("entityId") int entityId,
                               @RequestParam(value = "offset", defaultValue = "0") int offset,
                               @RequestParam(value = "count", defaultValue = "10") int count) {
        if (hostHolder.getUser() == null) {
            return Tool.GetJSONString(false, "未登陆");
        }

        try {
            List<Comment> comments = commentDao.showComment(entityType, entityId, offset, count);
            JSONObject ret = Tool.GetJSON(true);
            JSONArray commentArray = new JSONArray();
            for (Comment comment : comments) {
                User user = userDao.selectById(comment.getUserId());
                if (user == null) {
                    continue;
                }

                JSONObject obj = new JSONObject();
                obj.put("cid", comment.getId());
                obj.put("content", comment.getContent());
                obj.put("likeCount", comment.getLikeCount());
                obj.put("username", user.getName());
                obj.put("userid", user.getId());
                obj.put("userhead", user.getHeadUrl());
                obj.put("liked", likeService.isLiked( EntityType.COMMENT.getValue(),comment.getId(), hostHolder.getUser().getId()));
                commentArray.add(obj);
            }
            ret.put("comments", commentArray);
            return ret.toJSONString();
        } catch (Exception e) {
            return Tool.GetJSONString(false, "系统异常");
        }

    }


   /* @RequestMapping(value = "/addComment",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam(value = "content")String content,
                             @RequestParam(value = "file",required = false)MultipartFile[]files,
                             @RequestParam(value = "entityType")int entityType,
                             @RequestParam(value = "entityId")int entityId) {
        if(hostHolder.getUser() == null){

            return Tool.getJSONString(1,"登录状态才能发评论");

        }

        int userId = hostHolder.getUser().getId();

        return commentService.addComment(entityType,entityId,userId,files,content);
    }*/
    @RequestMapping(value = "/showComment")
    @ResponseBody
    public String showComment(@RequestParam(value = "entityType") int entityType ,
                              @RequestParam(value = "entityId") int entityId,
                              @RequestParam(value = "offset")int offset,
                              @RequestParam(value = "count")int count){

        String res = commentService.showComment(entityType,entityId,offset,count);

        if(res == null) return Tool.getJSONString(1,"没有评论");
        else return  Tool.getJSONString(0,res);

    }


    @RequestMapping(value = "/deleteComment")
    @ResponseBody
    public String deleteComment(@RequestParam(value = "id")int commentId){

        if(hostHolder.getUser() == null) return Tool.getJSONString(1,"请先登录");

        return commentService.deleteComment(commentId,hostHolder.getUser().getId());

    }
}
