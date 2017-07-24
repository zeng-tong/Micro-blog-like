package com.zengtong.DAO;

import com.zengtong.model.Weibo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface WeiboDao {
    int DELETE = 1;

    String TABLE_NAME = "weibo"; // 重复次数

    String INSERT_FIELDS = " user_id , status , comment_count, pic_url, create_date, content, like_count";

    String SELECT_FIELDS = "id ," + INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(", INSERT_FIELDS,") values(#{userId},#{status},#{commentCount},#{picUrl},#{createDate}, #{content}, #{likeCount})"})
    int insertWeibo(Weibo weibo);


}
