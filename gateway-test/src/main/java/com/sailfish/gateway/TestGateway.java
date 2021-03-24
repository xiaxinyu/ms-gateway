package com.sailfish.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author XIAXINYU3
 * @date 2021.3.24
 */
@EnableEurekaClient
@SpringBootApplication
public class TestGateway {
    public static void main(String[] args) {
        SpringApplication.run(TestGateway.class, args);
    }
}