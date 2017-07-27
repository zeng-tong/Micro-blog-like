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
    String INSERT_FIELDS = " content , from_id, to_id, has_read, conversation_id ,from_delete, to_delete ,create_date";
    String SELECT_FIELDS = "id ," + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME , "(", INSERT_FIELDS ," ) values (#{content},#{fromId},#{toId},#{hasRead},#{conversationId},#{fromDelete},#{toDelete},#{createDate} )"})
    int addMessage(Message message);


    @Select({"select " ,SELECT_FIELDS , " from ",TABLE_NAME , " where conversation_id=#{conversationId} order by create_date desc"})
    List<Message> showDetailMessage(String conversationId);

}
