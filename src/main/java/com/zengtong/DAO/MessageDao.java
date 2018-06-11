package com.zengtong.DAO;

import com.zengtong.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MessageDao {

    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " content , from_id, to_id, has_read, conversation_id ,from_delete, to_delete ,created_date";
    String SELECT_FIELDS = "id ," + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME , "(", INSERT_FIELDS ," ) values (#{content},#{fromId},#{toId},#{hasRead},#{conversationId},#{fromDelete},#{toDelete},#{createdDate} )"})
    int addMessage(Message message);

    @Select({"select " ,SELECT_FIELDS , " from ",TABLE_NAME , " where conversation_id=#{conversationId} order by created_date desc"})
    List<Message> showDetailMessage(String conversationId);

    @Select({"select count(id) as id, " ,INSERT_FIELDS , " from ","( select * from ",TABLE_NAME ,"where to_id = #{myId} or from_id=#{myId} order by created_date desc ) t group by conversation_id order by created_date desc"})
    List<Message> showListMessage(int myId);
}
