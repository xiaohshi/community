package com.nowcoder.community;

import com.nowcoder.community.config.AlphaConfig;
import com.nowcoder.community.config.QuartzConfig;
import com.nowcoder.community.quartz.AlphaJob;

import org.junit.jupiter.api.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import static org.quartz.TriggerBuilder.newTrigger;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class QuartzTest {

    @Autowired
    private Scheduler scheduler;

    @Test
    public void  testDeleteJob() throws SchedulerException {
        boolean b = scheduler.deleteJob(new JobKey("alphaJob", "alphaJobGroup"));
        System.out.println(b);
    }

    @Test
    public void testAddJob() throws SchedulerException, InterruptedException {
        JobDetail jobDetail = JobBuilder
                .newJob(AlphaJob.class)
                .withIdentity("alphaJob", "alphaJobGroup")
                .storeDurably()
                .build();
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .forJob(jobDetail)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInSeconds(2))
                .build();
        scheduler.addJob(jobDetail, true);
        scheduler.scheduleJob(trigger);
        scheduler.start();

        Thread.sleep(10000);
    }

}
