package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitive() {
        String text = "这里可以赌博，这里可以嫖娼，这里可以吸毒， 这里可以开票，哈哈哈";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }

}
