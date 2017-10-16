package com.nowcoder.dao;

import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageDao {
    String TABLE_NAME = "message" ;
    String INSERT_FILEDS = "from_id, to_id, conversation_id, content, created_date, has_read" ;
    String SELECTED_FILEDS = "id" + INSERT_FILEDS ;

    @Insert({"insert into ", TABLE_NAME, " (", INSERT_FILEDS, ") values (#{fromId}, #{toId}, " +
            "#{conversationId}, #{content}, #{createdDate}, #{hasRead} )" })
    int addMessage(Message message) ;

    @Select({"select ",SELECTED_FILEDS ," from", TABLE_NAME,
            "where conversation_id = #{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit) ;

    @Select({"select count(id) from  ", TABLE_NAME,
            " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId,@Param("conversationId") String conversationId) ;


    @Select({"select ", INSERT_FILEDS, " , count(id) as id from (select * from ", TABLE_NAME, "  where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit) ;
}
