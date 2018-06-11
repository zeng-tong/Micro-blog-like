package com.zengtong.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zengtong.Async.EventProducer;
import com.zengtong.DAO.UserDao;
import com.zengtong.Service.*;
import com.zengtong.Utils.Tool;
import com.zengtong.model.EntityType;
import com.zengtong.model.HostHolder;
import com.zengtong.model.User;
import com.zengtong.model.Weibo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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

    @RequestMapping(value = "/redirect/{code}")
    public String redirect(@PathVariable("code") int code,
                           HttpSession httpSession) {

    /*    RedirectView red = new RedirectView("/",true);

        if(code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }*/

        httpSession.setAttribute("info", "Jump from redirect");

        return "redirect:/";

    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e) {
        return "error: " + e.getMessage();
    }


    @RequestMapping(value = "/")
    public String index(Model model) {
        int userID = 1;
        if (hostHolder.getUser() != null) {
            userID = hostHolder.getUser().getId();
        }
        try {
//            model.addAttribute("weibos",getWeibos(userID,0,10));
            model.addAttribute("user", hostHolder.getUser());
            model.addAttribute("weibos", getWeibos(weiboService.getFeed(userID, 0, 10)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }

    @RequestMapping(path = {"/profile/{uid}"})
    public String profile(@PathVariable("uid") int uid, Model model) {

        if (hostHolder.getUser() == null) {
            return "redirect:/";
        }

        try {
            model.addAttribute("weibos", getWeibos(uid, 0, 10));
            model.addAttribute("user", hostHolder.getUser());
        } catch (Exception e) {

        }
        return "profile";
    }

    @RequestMapping(value = "/followee/{uid}")
    public String followee(@PathVariable("uid") int uid, Model model,
                           @RequestParam(value = "offset", defaultValue = "0") int offset,
                           @RequestParam(value = "count", defaultValue = "10") int count) {
        try {
            User user = userDao.selectById(uid);
            if (user == null) {
                return "redirect:/index";
            }
            model.addAttribute("hostname", user.getName());
            model.addAttribute("followeeCount", followService.getFolloweeCount(uid)); //EntityType zan shi mei you shiyong.
            model.addAttribute("user", hostHolder.getUser());
            List<Integer> followeeIds = followService.getFollowees(uid, offset, count);
            model.addAttribute("vos", getFollowUsers(followeeIds));

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "followee";
    }

    @RequestMapping(path = {"/follower/{uid}"})
    public String follower(@PathVariable("uid") int uid, Model model,
                           @RequestParam(value = "offset", defaultValue = "0") int offset,
                           @RequestParam(value = "count", defaultValue = "10") int count) {
        try {
            User u = userDao.selectById(uid);
            if (u == null) {
                return "redirect:/";
            }
            model.addAttribute("hostname", u.getName());
            model.addAttribute("followerCount", followService.getFollowerCount(uid));
            model.addAttribute("user", hostHolder.getUser());
            List<Integer> followerIds = followService.getFollowers(uid, offset, count);
            model.addAttribute("vos", getFollowUsers(followerIds));

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "follower";
    }

    private JSONArray getFollowUsers(List<Integer> followIds) {
        JSONArray vos = new JSONArray();
        for (int id : followIds) {
            JSONObject obj = new JSONObject();
            User followeeUser = userDao.selectById(id);
            if (followeeUser == null) {
                continue;
            }
            obj.put("userhead", followeeUser.getHeadUrl());
            obj.put("userid", followeeUser.getId());
            obj.put("username", followeeUser.getName());
            if (hostHolder.getUser() != null) {
                obj.put("followed", followService.isFollower(hostHolder.getUser().getId(), followeeUser.getId()));
            } else {
                obj.put("followed", false);
            }
            vos.add(obj);
        }
        return vos;
    }


    @RequestMapping(path = {"/weibo"}, method = {RequestMethod.POST})
    @ResponseBody

    public String addWeibo(@RequestParam("content") String content, @RequestParam(value = "images", defaultValue = "") String images) {
        System.out.println("addWeibo: " + images);
        try {
            //Todo : change the upload images interface.
            if (hostHolder.getUser() == null) {
                return Tool.GetJSONString(false, "请先登陆.");
            }

            int wid = weiboService.UpWeibo(hostHolder.getUser().getId(), content, images);

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
                vo.put("liked", likeService.isLiked(EntityType.WEIBO.getValue(), wb.getId(), hostHolder.getUser().getId()));
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
