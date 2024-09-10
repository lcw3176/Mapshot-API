package com.mapshot.api.presentation.externalLog;

import com.mapshot.api.application.log.LogUseCase;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class ExternalLogCollectorController {

    private final LogUseCase logUseCase;

    // fixme 임시로 변경
    @PreAuth(Accessible.EVERYONE)
    @PostMapping
    public void collectExternalLog(@RequestBody LogRequest request) {
        logUseCase.collectLog(request.getRoll(), request.getStackTrace());
    }
}
