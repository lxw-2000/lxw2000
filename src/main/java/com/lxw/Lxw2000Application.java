package com.lxw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//开启定时任务
@EnableScheduling
public class Lxw2000Application {

    public static void main(String[] args) {
        SpringApplication.run(Lxw2000Application.class, args);
    }

}
