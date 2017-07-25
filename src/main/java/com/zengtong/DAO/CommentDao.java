package com.zengtong.DAO;


import com.zengtong.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CommentDao {
    String TABLE_NAME = "comment";

    String INSERT_FIELDS = " entity_type,entity_id,content,pic_url,status,create_date,user_id,like_count,reply_count";

    String SELECT_FIELDS = "id" + INSERT_FIELDS;

    @Insert({"insert into " ,TABLE_NAME,"(",INSERT_FIELDS,")  values(#{entityType},#{entityId},#{content},#{picUrl},#{status},#{createDate},#{userId},#{likeCount},#{replyCount})"})
    int addComment(Comment comment);


}
