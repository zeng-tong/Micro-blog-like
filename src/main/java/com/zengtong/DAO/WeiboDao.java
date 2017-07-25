package com.zengtong.DAO;

import com.zengtong.model.Weibo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WeiboDao {
    int DELETE = 1;

    String TABLE_NAME = "weibo"; // 重复次数

    String INSERT_FIELDS = " user_id , status , comment_count, pic_url, create_date, content, like_count";

    String SELECT_FIELDS = "id ," + INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(", INSERT_FIELDS,") values(#{userId},#{status},#{commentCount},#{picUrl},#{createDate}, #{content}, #{likeCount})"})
    int insertWeibo(Weibo weibo);

    @Select({"select ",SELECT_FIELDS, "from ",TABLE_NAME, "where user_id=#{userId} and status = 0 limit #{offset},#{count}"})
    List<Weibo> showWeiboByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("count") int count);

    @Select({"select ",SELECT_FIELDS, "from ",TABLE_NAME," where status = 0 limit #{offset},#{count}"})
    List<Weibo> showAllWeibo(@Param("offset") int offset, @Param("count") int count);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,"where id=#{id}"})
    Weibo selectWeiboById(Integer id);

    @Update({"update ", TABLE_NAME ,"set status=1 where id=#{id}"})
    void deleteWeibo(Integer id);

    @Update({"update ",TABLE_NAME," set comment_count=comment_count+1 where id = #{weiboId}"})
    void addCommentCount(int weiboId);
}
