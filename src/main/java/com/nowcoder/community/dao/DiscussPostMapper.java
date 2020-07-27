package com.nowcoder.community.dao;

import java.util.List;

import com.nowcoder.community.entity.DiscussPost;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectAll(int userId, int offset, int limit);

    //@Param用于给参数取别名，如果只有一个参数，并且在<if>里使用，则必须要使用别名
    int getTotalCount(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost post);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);

    int updateType(int id, int type);

    int updateStatus(int id, int status);

}
