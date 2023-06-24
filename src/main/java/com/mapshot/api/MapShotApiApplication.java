package com.mapshot.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MapShotApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapShotApiApplication.class, args);
    }

}
