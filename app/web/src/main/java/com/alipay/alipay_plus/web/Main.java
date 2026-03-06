package com.alipay.alipay_plus.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.alipay.account")
@MapperScan("com.alipay.account.common.dal.auto.custom") // <- package of your DAOs
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}