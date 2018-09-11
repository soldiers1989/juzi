package com.jzfq.retail.core;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * app服务启动的入口
 * @author: liuwei
 * @version: 1.0
 * @date: 2018/7/16 14:00
 */
@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@EnableAdminServer
public class SpringBootAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdminApplication.class, args);
		System.out.println("======================SpringBootAdminApplication Start Success=====================");
	}
}
