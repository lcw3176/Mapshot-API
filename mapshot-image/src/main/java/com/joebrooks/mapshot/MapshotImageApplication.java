package com.joebrooks.mapshot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MapshotImageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapshotImageApplication.class, args);
    }
}
