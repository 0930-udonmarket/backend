package com.udonmarket.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UdonmarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(UdonmarketApplication.class, args);
    }
}