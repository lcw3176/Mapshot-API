package com.joebrooks.mapshot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class MapshotNoticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapshotNoticeApplication.class, args);
    }

}
