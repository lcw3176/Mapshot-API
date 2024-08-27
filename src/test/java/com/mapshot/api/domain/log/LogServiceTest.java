package com.mapshot.api.domain.log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class LogServiceTest {

    @Autowired
    private LogService logService;

    @Test
    public void 로그_작성_테스트(CapturedOutput output) {
        logService.write("testRoll", "testLog");
        assertThat(output).contains("testRoll", "testLog");
    }
}