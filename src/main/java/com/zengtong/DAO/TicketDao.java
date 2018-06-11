package com.zengtong.DAO;


import com.zengtong.model.Ticket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface TicketDao {

    String TABLE_NAME = " Ticket ";

    String SELECT_FIEILD = " id , user_id , ticket , status, expired ";

    String INSERT_FIEILD = " user_id , ticket, status , expired ";

    @Insert({"insert into ",TABLE_NAME," ( ", INSERT_FIEILD ,") values (#{userid},#{ticket},#{status},#{expired})"})
    int addTicket(Ticket ticket);

    @Select({"select ",SELECT_FIEILD," from ", TABLE_NAME ," where ticket=#{ticket}"})
    Ticket selectByTicket(String ticket);

    @Update({"update ",TABLE_NAME," set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket")String ticket,@Param("status")int status);



}
