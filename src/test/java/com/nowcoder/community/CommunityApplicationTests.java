package com.nowcoder.community;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.service.AlphaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	@SneakyThrows
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Test
	void contextLoads() {
		System.out.println(applicationContext);

		AlphaDao bean = applicationContext.getBean(AlphaDao.class);
		System.out.println(bean.select());

		AlphaDao alphaSxh = applicationContext.getBean("alphaSxh", AlphaDao.class);
		System.out.println(alphaSxh.select());
	}

	@Test
	void testService() {
		AlphaService bean = applicationContext.getBean(AlphaService.class);
		bean.get();
	}

	@Test
	void testConfig() {
		SimpleDateFormat bean = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(bean.format(new Date()));
	}

	@Autowired
	private AlphaDao alphaDao;

	@Autowired
	private AlphaService alphaService;

	@Test
	void testDI() {
		System.out.println(alphaDao.select());
	}

	@Autowired
	private LoginTicketMapper loginTicketMapper;

	@Test
	public void testLoginTicket() {
		LoginTicket loginTicket = LoginTicket.builder()
				.userId(101).ticket("sfsdfs")
				.expired(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
				.build();
		loginTicketMapper.insertLoginTicket(loginTicket);
	}

}