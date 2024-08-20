package com.mapshot.api.application.log;

import com.mapshot.api.domain.log.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogUseCase {

    private final LogService logService;

    public void collectLog(String roll, String stackTrace) {
        logService.write(roll, stackTrace);
        logService.alert(roll, stackTrace);
    }
}
