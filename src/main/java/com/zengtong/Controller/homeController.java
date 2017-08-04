package com.zengtong.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zengtong.AOP.LoginRequired;
import com.zengtong.Async.EventProducer;
import com.zengtong.DAO.CommentDao;
import com.zengtong.DAO.UserDao;
import com.zengtong.Service.*;
import com.zengtong.Utils.Tool;
import com.zengtong.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


/**
 * Created by znt on 17-7-18.
 */
@Controller
public class homeController {

    private static Logger logger = LoggerFactory.getLogger(homeController.class);


    @Autowired
    HostHolder hostHolder;

    @Autowired
    WeiboService weiboService;

    @Autowired
    UserSercvice userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;


    @Autowired
    EventProducer eventProducer;
    /**
     * thymleaf
     * */
    @RequestMapping(value = "/")
    public String home(HttpSession session){
    /*    model.addAttribute("name","ZengTong");
        model.addAttribute("user",new User("小哥",10,"pswd"));*/



        String name =  "NULL";

        if(hostHolder.getUser() != null){
            name = hostHolder.getUser().getName();
        }

        session.setAttribute("name",name);


        System.out.println("This is controller");



        return "home";

    }


    @RequestMapping(value = "/redirect/{code}")
    public String redirect(@PathVariable("code") int code,
                                 HttpSession httpSession){

    /*    RedirectView red = new RedirectView("/",true);

        if(code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }*/

        httpSession.setAttribute("info","Jump from redirect");

        return "redirect:/";

    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "error: " + e.getMessage();
    }



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
    @RequestMapping(path = {"/listComments"}, method = {RequestMethod.POST, RequestMethod.GET})
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
                obj.put("userhead", user.getHead_url());
                obj.put("liked", likeService.isLiked(hostHolder.getUser().getId(), EntityType.COMMENT.getValue(), comment.getId()));
                commentArray.add(obj);
            }
            ret.put("comments", commentArray);

            return ret.toJSONString();
        } catch (Exception e) {
            return Tool.GetJSONString(false, "系统异常");
        }

    }


    @RequestMapping(value = "/index")
    public String index(Model model){

        int userID = 1;

        if(hostHolder.getUser()!=null){
            userID = hostHolder.getUser().getId();
        }

        try {
            model.addAttribute("weibos",getWeibos(userID,0,10));
            model.addAttribute("user",hostHolder.getUser());
        }catch (Exception e){
            e.printStackTrace();
        }

        return "index";
    }

    @RequestMapping(path = {"/profile/{uid}"})
    public String profile(@PathVariable("uid") int uid, Model model) {
        try {
            model.addAttribute("weibos", getWeibos(uid, 0, 10));
        } catch (Exception e) {

        }
        return "profile";
    }

    @RequestMapping(value = "/followee/{uid}")
    public String followee(@PathVariable("uid") int uid, Model model,
                           @RequestParam(value = "offset", defaultValue = "0") int offset,
                           @RequestParam(value = "count", defaultValue = "10") int count){
        try {
            User user = userDao.selectById(uid);
            if (user == null){
                return "redirect:/index";
            }
            model.addAttribute("hostname",user.getName());
            model.addAttribute("followeeCount",followService.getFolloweeCount(uid,EntityType.USER.getValue())); //EntityType zan shi mei you shiyong.
            List<Integer> followeeIds = followService.getFollowees(uid, offset, count);
            model.addAttribute("vos", getFollowUsers(followeeIds));

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return "followee";
    }
    private JSONArray getFollowUsers(List<Integer> followIds) {
        JSONArray vos = new JSONArray();
        for (int id : followIds) {
            JSONObject obj = new JSONObject();
            User followeeUser = userDao.selectById(id);
            if (followeeUser == null) {
                continue;
            }
            obj.put("userhead", followeeUser.getHead_url());
            obj.put("userid", followeeUser.getId());
            obj.put("username",followeeUser.getName());
            if (hostHolder.getUser() != null) {
                obj.put("followed", followService.isFollower(hostHolder.getUser().getId(),followeeUser.getId()));
            } else {
                obj.put("followed", false);
            }
            vos.add(obj);
        }
        return vos;
    }

    @RequestMapping(path = {"/addWeibo"}, method = {RequestMethod.POST})
    @ResponseBody

    public String addWeibo(@RequestParam("content") String content, @RequestParam("images") String images) {
        try {
            //To do : change the upload images interface.

            int wid = weiboService.UpWeibo(hostHolder.getUser().getId(),content,images);
            return Tool.GetJSONString(wid > 0);
        } catch (Exception e) {
            return Tool.GetJSONString(false, "系统异常");
        }
    }

    @Autowired
    private UserDao userDao;

    private JSONArray getWeibos(List<Weibo> weibos) {

        JSONArray vos = new JSONArray();

        for (Weibo wb : weibos) {
            JSONObject vo = new JSONObject();
            vo.put("wb", wb);
            vo.put("user", userDao.selectById(wb.getUserId()));
            vo.put("images", wb.getImageList());
            if (hostHolder.getUser() != null) {
                vo.put("liked", likeService.isLiked(hostHolder.getUser().getId(), EntityType.WEIBO.getValue(), wb.getId()));
                vo.put("followed", followService.isFollower(hostHolder.getUser().getId(), wb.getUserId()));
            } else {
                vo.put("liked", false);
                vo.put("followed", false);
            }
            vos.add(vo);
        }
        return vos;
    }

    private JSONArray getWeibos(int userId, int offset, int count) {
        List<Weibo> weibos = weiboService.ListWeiboByUserId(userId, offset, count);
        return getWeibos(weibos);
    }


}
