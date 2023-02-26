package com.joebrooks.image;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = {TestConfiguration.class})
@SpringBootTest
class MapshotImageApplicationTests {

    @Test
    void contextLoads() {
    }

}
