package com.sailfish.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ApplicationGateway {
	public static void main(String[] args) {
		SpringApplication.run(ApplicationGateway.class, args);
	}
}