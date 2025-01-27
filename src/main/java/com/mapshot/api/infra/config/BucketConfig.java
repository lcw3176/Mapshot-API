package com.mapshot.api.infra.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class BucketConfig {

    private static final int TOKENS = 10;

    @Bean
    public Bucket getBucket(){
        Bandwidth limit = Bandwidth.builder()
                    .capacity(TOKENS)
                    .refillGreedy(TOKENS, Duration.ofMinutes(1))
                    .build();

        return Bucket.builder().addLimit(limit).build();
    }
}
