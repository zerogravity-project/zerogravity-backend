package com.zerogravity.myapp.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

// MyBatis mapper scanning configuration for domain-based architecture
@Configuration
@MapperScan(basePackages = "com.zerogravity.myapp")
public class DBConfig {
    public static void main(String[] args) {

    }
}