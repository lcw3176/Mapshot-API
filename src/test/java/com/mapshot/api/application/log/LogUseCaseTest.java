package com.mapshot.api.application.log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class LogUseCaseTest {

    @Autowired
    private LogUseCase logUseCase;


    @Test
    void 로그_수집_후_슬랙으로_전송한다(CapturedOutput output) {
        logUseCase.collectLog("testRoll", "testStackTrace");
        assertThat(output).contains("testRoll", "testStackTrace");
    }

}