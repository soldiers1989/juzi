package com.jzfq.retail.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * app服务启动的入口
 * @author: liuwei
 * @version: 1.0
 * @date: 2018/7/16 14:00
 */
@Slf4j
@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
		log.info("+========================================");
	}
}
