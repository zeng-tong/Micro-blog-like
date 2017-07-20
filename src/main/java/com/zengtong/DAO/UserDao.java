package com.zengtong.DAO;

import com.zengtong.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by znt on 17-7-18.
 */
@Service
@Mapper
public interface UserDao {
    String TABLE_NAME = "User";
    String INSET_FIELDS = "Username, Age ,Password";
    String SELECT_FIELDS = "Username,Age";
    @Insert({"insert into",TABLE_NAME,"(",INSET_FIELDS,") values(#{Username},#{Age},#{Password})"})
    int addUser(User user);

//    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
}