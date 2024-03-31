package com.seminav.marketapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MarketAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketAppApplication.class, args);
    }

}
