package com.jzfq.retail.core.task.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@Slf4j
@ImportResource(locations = {"classpath:dubbo-*.xml"})
@EnableAutoConfiguration
@SpringBootApplication
@ServletComponentScan
@ComponentScan(basePackages = "com.jzfq.retail.core")
public class TaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
		log.info("======================TaskApplication Start Success=====================");
	}

}
