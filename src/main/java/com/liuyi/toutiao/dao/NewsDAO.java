package com.liuyi.toutiao.dao;

import com.liuyi.toutiao.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count,created_date, user_id ";
    String SELECT_FIELDS = " id, title, link, image, like_count, comment_count, created_date, user_id ";

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title}, #{link}, #{image}, #{likeCount}, #{commentCount}, #{createdDate}, #{userId})"})
    int addNews(News news);

    List<News> selectByUserIdAndOffset(@Param("userId") int userId,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{newsId}"})
    News getNewsById(@Param("newsId") int newsId);

    @Update({"update ", TABLE_NAME, " set comment_count=#{commentCount} where id=#{newsId}"})
    int updateNewsCommentCount(@Param("commentCount") int commentCount, @Param("newsId") int newsId);

    @Update({"update ", TABLE_NAME, " set like_count=#{likeCount} where id=#{id}"})
    int updateNewsLikeCount(@Param("id") int id, @Param("likeCount") int likeCount);

}
