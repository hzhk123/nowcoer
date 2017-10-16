package com.nowcoder.dao;

import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by nowcoder on 2016/7/2.
 */


@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = "login_ticket";
    String INSERT_FIELDS = " user_id, status,  expired, ticket";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{status},#{expired},#{ticket})"})
    int addLoginTicket(LoginTicket loginTicket);

    @Select({"select ", SELECT_FIELDS, "from ",TABLE_NAME ,"where ticket= #{ticket}"})
    LoginTicket selectByTicket(String ticket) ;

    @Update({"update ", TABLE_NAME, " set status = #{status} where ticket = #{ticket}"})
    void updateStatus(@Param("ticket") String ticket,
                      @Param("status") int status) ;

}
