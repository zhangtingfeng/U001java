package com.hst.ii;

import com.hst.core.EnableHst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@EnableHst
//@ImportAutoConfiguration({HstConfiguration.class,
//        DaoAutoConfiguration.class,
//        MetaAutoConfiguration.class
//})
public class PortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortalApplication.class, args);
    }
}
