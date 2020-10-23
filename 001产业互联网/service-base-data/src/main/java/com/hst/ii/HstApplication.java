package com.hst.ii;

import com.hst.core.EnableHst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@EnableHst
public class HstApplication {
    public static void main(String[] args) {
        SpringApplication.run(HstApplication.class, args);
    }
}
