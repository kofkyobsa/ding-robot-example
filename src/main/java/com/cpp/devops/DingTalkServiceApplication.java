package com.cpp.devops;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@MapperScan("com/cpp/devops/mapper")
public class DingTalkServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DingTalkServiceApplication.class, args);
    }

}