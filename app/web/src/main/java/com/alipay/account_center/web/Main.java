package com.alipay.account_center.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.alipay.account_center"})
@MapperScan("com.alipay.account_center.common.dal.auto.custom")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}