package com.nowcoder.community.dao;

import java.util.List;

import com.nowcoder.community.entity.Message;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {

    // 查询当前用户会话列表，针对每个会话只返回一条最新的私信
    List<Message> selectConversations(int userId, int offset, int limit);

    //查询当前用户的会话数量
    int selectConversationCount(int userId);

    //查询某个会话所包含的私信列表
    List<Message> selectLetter(String conversationId, int offset, int limit);

    //查询某个会话所包含的私信
    int selectLetterCount(String conversationId);

    //查询未读私信数量
    int selectLetterUnreadCount(int userId, String conversation);

}
