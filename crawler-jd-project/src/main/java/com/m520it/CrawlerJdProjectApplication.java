package com.m520it;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//使用定时任务,需要先开启定时任务,需要添加注解
@EnableScheduling
public class CrawlerJdProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerJdProjectApplication.class, args);
    }

}
