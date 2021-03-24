package com.sailfish.gateway.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author XIAXINYU3
 * @date 2021.3.24
 */
@EnableEurekaClient
@SpringBootApplication
public class HelloGateway {
    public static void main(String[] args) {
        SpringApplication.run(HelloGateway.class, args);
    }
}