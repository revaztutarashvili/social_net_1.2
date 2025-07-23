package com.socialplatformapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SocialPlatformApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialPlatformApiApplication.class, args);
    }

}
