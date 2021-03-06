package com.liuyi.toutiao.dao;

import com.liuyi.toutiao.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {

    String TABLE_NAME = "message";
    String INSERT_FIELD = "from_id, to_id, content, conversation_id, has_read, created_date";
    String SELECT_FIELD = "id, " + INSERT_FIELD;

    @Insert({"insert into ", TABLE_NAME, " (", INSERT_FIELD,
            ") values (#{fromId}, #{toId}, #{content}, #{conversationId}, #{hasRead}, #{createdDate})"})
    int addMessage(Message message);

    @Select({"select ", SELECT_FIELD, " from ", TABLE_NAME,
            " where conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Select({"select t.id, ", INSERT_FIELD, " from ", TABLE_NAME,
            " as m right join (select max(id) as maxid, count(id) as id from ", TABLE_NAME,
            " group by conversation_id) as t on m.id=t.maxid order by t.maxid desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    @Select({"select count(has_read) from ", TABLE_NAME,
            " where conversation_id=#{conversationId} and has_read=#{hasRead} and to_id=#{toId}"})
    int getReadStatusCount(@Param("conversationId") String conversationId,
                           @Param("hasRead") int hasRead,
                           @Param("toId") int toId);

    @Update({"update ", TABLE_NAME, " set has_read=#{readStatus} where conversation_id=#{conversationId}"})
    void updateReadStatus(@Param("conversationId") String conversationId,
                          @Param("readStatus") int readStatus);

}
