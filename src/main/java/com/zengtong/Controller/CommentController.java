package com.zengtong.Controller;


import com.zengtong.Service.CommentService;
import com.zengtong.Utils.Tool;
import com.zengtong.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CommentController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/addComment",method = {RequestMethod.GET,RequestMethod.POST})
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
    }
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
