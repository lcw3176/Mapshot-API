package com.joebrooks.mapshot.assembler.controller;

import com.joebrooks.mapshot.assembler.model.ImageRequest;
import com.joebrooks.mapshot.assembler.service.ImageAssemblerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AssemblerController {
    
    private final ImageAssemblerService imageAssemblerService;

    @MessageMapping("/api/image/assembler")
    public void addUserRequestToQueue(@Payload ImageRequest request, @Header("simpSessionId") String sessionId) {
        request.setSessionId(sessionId);
        imageAssemblerService.addToQueue(request);
    }

}
