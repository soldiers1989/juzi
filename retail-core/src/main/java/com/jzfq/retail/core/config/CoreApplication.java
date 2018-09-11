package com.jzfq.retail.core.config;

import com.jzfq.retail.core.datasource.DynamicDataSourceRegister;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 核心服务启动入口
 * @author: liuwei
 * @version: 1.0
 * @date: 2018/7/15 13:28
 */
@Slf4j
@MapperScan("com.jzfq.retail.core.dao")
@ImportResource(locations = {"classpath:dubbo-*.xml"})
@ComponentScan(basePackages = "com.jzfq.retail")
@Import(DynamicDataSourceRegister.class)
@PropertySource("classpath:module.yml")
@EnableAutoConfiguration
@SpringBootApplication
public class CoreApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CoreApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
		log.info("======================CoreApplication Start Success=====================");
	}
}
