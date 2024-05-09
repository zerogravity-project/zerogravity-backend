package com.zerogravity.myapp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.zerogravity.myapp.model.dao")
public class DBConfig {
    public static void main(String[] args) {
        
    }
}