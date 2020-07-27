package com.nowcoder.community;

import java.util.List;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MybatisTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testMybatis() {
        User user = userMapper.selectById(1);
        System.out.println(user);

        int xiaohshi = userMapper.updateHeader(1, "xiaohshi");
        System.out.println(xiaohshi);

        User sxh = User.builder().username("sxh").password("1234").email("2324138412@qq.com").build();
        int i = userMapper.insertUser(sxh);
        System.out.println(i);
    }

    @Test
    public void testDiscussPost() {
        int totalCount = discussPostMapper.getTotalCount(149);
        System.out.println(totalCount);

        List<DiscussPost> discussPosts = discussPostMapper.selectAll(149, 0, 10);
        discussPosts.forEach(System.out::println);
    }
}
