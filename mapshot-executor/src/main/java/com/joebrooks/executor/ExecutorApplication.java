package com.joebrooks.executor;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.joebrooks")
@EnableJpaRepositories(basePackages = "com.joebrooks")
@EntityScan("com.joebrooks")
public class ExecutorApplication {

    public static final String PROFILE_LOCATIONS = "spring.config.location="
            + "./mapshot-notice/src/main/resources/application.yml,"
            + "./mapshot-image/src/main/resources/application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(ExecutorApplication.class)
                .properties(PROFILE_LOCATIONS)
                .run(args);
    }
}
