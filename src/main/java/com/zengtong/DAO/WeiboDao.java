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
    Long insertWeibo(Weibo weibo);

    @Select({"select ",SELECT_FIELDS, "from ",TABLE_NAME, "where user_id=#{userId} and status = 0 limit #{offset},#{count}"})
    List<Weibo> showWeiboByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("count") int count);

    @Select({"select ",SELECT_FIELDS, "from ",TABLE_NAME," where status = 0 limit #{offset},#{count}"})
    List<Weibo> showAllWeibo(@Param("offset") int offset, @Param("count") int count);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,"where id=#{id}"})
    Weibo selectWeiboById(Integer id);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,"where user_id=#{id} order by id limit 0,1 "})
    Weibo selectOneByUserID(Integer id);

    @Select({"select ",SELECT_FIELDS ," from ",TABLE_NAME , "where status=0 order by like_count desc limit #{offset},#{limit}"}) // TO DO : 筛选策略的选择.
    List<Weibo> selectByfavor(@Param("offset") int offset ,@Param("limit") int limit);

    @Update({"update ", TABLE_NAME ,"set status=1 where id=#{id}"})
    void deleteWeibo(Integer id);

    @Update({"update ",TABLE_NAME," set comment_count=comment_count+1 where id = #{weiboId}"})
    void addCommentCount(int weiboId);

    @Update({"update ",TABLE_NAME," set like_count=like_count+1 where id = #{weiboId}"})
    void addLikeCount(int weiboId);


    @Update({"update ",TABLE_NAME," set like_count=like_count-1 where id = #{weiboId}"})
    void minusLikeCount(int weiboId);



    @Update({"update ",TABLE_NAME," set comment_count=comment_count-1 where id = #{weiboId}"})
    void minusCommentCount(int weiboId);



}
