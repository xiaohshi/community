package com.nowcoder.community;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.nowcoder.community.service.AlphaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;

@Slf4j
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ThreadPoolTest {

    // JDK普通线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    // JDK可执行定时任务的线程池
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    // Spring普通线程池
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    // Spring可执行定时任务的线程池
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private AlphaService alphaService;

    // JDK普通的线程池
    @Test
    public void testExecutorService() {
        Runnable task = () -> log.info("hello executorService");

        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }

        sleep(10000);
    }

    // JDK可执行定时任务的线程池
    @Test
    public void testScheduledExecutorService() {
        scheduledExecutorService.scheduleAtFixedRate(() -> log.info("hello scheduledExecutorService"),
                10000, 1000, TimeUnit.MILLISECONDS);

        sleep(20000);
    }

    // Spring普通线程池
    @Test
    public void testThreadPoolTaskExecutor() {
        for (int i = 0; i < 10; i++) {
            taskExecutor.execute(() -> log.info("Hello ThreadPoolTaskExecutor"));
        }
        sleep(10000);
    }

    // Spring可执行定时任务的线程池
    @Test
    public void testThreadPoolTaskScheduler() {
        Date startTime = new Date(System.currentTimeMillis() + 10000);

        taskScheduler.scheduleAtFixedRate(() -> log.info("Hello ThreadPoolTaskScheduler"),
                startTime, 1000);

        sleep(30000);
    }

    // Spring普通线程池简化模式
    @Test
    public void testAsyn() {
        for (int i = 0; i < 10; i++) {
            alphaService.execute();
        }
        sleep(10000);
    }

    // Spring可执行定时任务的线程池简化
    @Test
    public void testScheduled() {
        sleep(30000);
    }

    private void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
