package com.zengtong.DAO;


import com.zengtong.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface CommentDao {
    String TABLE_NAME = " comment ";

    String INSERT_FIELDS = " entity_type, entity_id, content,pic_url,status,create_date,user_id,like_count,reply_count ";

    String SELECT_FIELDS = " id ," + INSERT_FIELDS;

    @Insert({"insert into " ,TABLE_NAME,"(",INSERT_FIELDS,")  values(#{entityType},#{entityId},#{content},#{picUrl},#{status},#{createDate},#{userId},#{likeCount},#{replyCount})"})
    int addComment(Comment comment);

    @Select({"select ",SELECT_FIELDS, "from",TABLE_NAME," where entity_type=#{entityType} and entity_id = #{entityId} and status=0 limit #{offset},#{count}"})
    List<Comment> showComment(@Param("entityType") int entityType,@Param("entityId") int entityId,@Param("offset")int offset,@Param("count")int count);


    @Update({"update ",TABLE_NAME,"set reply_count=reply_count+1 where id=#{id}"})
    void addCommentCount(int id);

    @Update({"update ",TABLE_NAME," set status=1 where id=#{id}"})
    void deleteComment(int id);

    @Select({"select ",SELECT_FIELDS, " from ",TABLE_NAME," where id=#{id} and status=0"})
    Comment selectCommentById(int id);

    @Update({"update ",TABLE_NAME,"set reply_count=reply_count-1 where id=#{id}"})
    void  minusCommentCount(int id);

    @Update({"update ",TABLE_NAME," set like_count=like_count+1 where id = #{commentID}"})
    void addLikeCount(int commentID);

    @Update({"update ",TABLE_NAME,"set like_count=like_count-1 where id=#{id}"})
    void  minusLikeCount(int id);





}
