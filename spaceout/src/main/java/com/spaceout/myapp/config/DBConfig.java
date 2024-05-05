package com.spaceout.myapp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.spaceout.myapp.model.dao")
public class DBConfig {
    public static void main(String[] args) {
        
    }
}