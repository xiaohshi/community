package com.nowcoder.community.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Service
public class AlphaService {
    public AlphaService() {
        System.out.println("Constructor");
    }

    @PostConstruct
    public void init() {
        System.out.println("Init");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Destroy");
    }

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public void get() {
        System.out.println("xiaohshi");
    }


    // REQUIRED：支持当前事务（外部事务），如果不存在，就创建新事物
    // REQUIRE_NEW：创建一个新的事务，并且暂停当前事务（外部事务）
    // NESTED：如果当前存在事务（外部事务），则嵌套在该事务中执行（独立的提交和回滚），否则就和REQUIRED一样
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1() {
        String salt = CommunityUtil.genetateUUID().substring(0, 5);
        // 新增用户
        User user = User.builder().username("alpha").salt(salt)
                .password(CommunityUtil.md5("123" + salt))
                .email("xx@xx.com").headerUrl("xxxxx").createTime(new Date()).build();
        userMapper.insertUser(user);

        // 新增帖子
        DiscussPost post = DiscussPost.builder().userId(user.getId()).title("test")
                .content("hello world").createTime(new Date()).build();
        discussPostMapper.insertDiscussPost(post);

        Integer.valueOf("sxh");
        return "ok";
    }

    public Object save2() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return transactionTemplate.execute(new TransactionCallback<Object>() {

            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                String salt = CommunityUtil.genetateUUID().substring(0, 5);
                // 新增用户
                User user = User.builder().username("alpha").salt(salt)
                        .password(CommunityUtil.md5("123" + salt))
                        .email("xx@xx.com").headerUrl("xxxxx").createTime(new Date()).build();
                userMapper.insertUser(user);

                // 新增帖子
                DiscussPost post = DiscussPost.builder().userId(user.getId()).title("test")
                        .content("hello world").createTime(new Date()).build();
                discussPostMapper.insertDiscussPost(post);

                Integer.valueOf("sxh");
                return "ok";
            }
        });
    }

    // 让该方法在多线程的环境下被异步的调用,这个方法就相当于一个线程体
    @Async
    public void execute() {
        log.info("hello");
    }

    // 自动启动
    //@Scheduled(initialDelay = 10000, fixedRate = 1000)
    public void execute2() {
        log.info("execute2");
    }
}
