package com.jzfq.retail.core.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * admin启动的入口
 * @author: liuwei
 * @version: 1.0
 * @date: 2018/7/16 13:59
 */
@Slf4j
@ImportResource(locations = {"classpath:dubbo-*.xml"})
@EnableAutoConfiguration
@SpringBootApplication
@EnableRedisHttpSession
@ServletComponentScan
@ComponentScan(basePackages = "com.jzfq.retail.core")
@EntityScan(basePackages = "com.jzfq.retail.core")
public class TouchApplication {

	public static void main(String[] args) {
		SpringApplication.run(TouchApplication.class, args);
		log.info("======================TouchApplication Start Success=====================");
	}
}
