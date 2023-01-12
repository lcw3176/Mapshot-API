package com.joebrooks.mapshot.assembler.controller;

import com.joebrooks.mapshot.assembler.model.ImageRequest;
import com.joebrooks.mapshot.assembler.model.ImageResponse;
import com.joebrooks.mapshot.assembler.service.ImageAssemblerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RestController
@RequiredArgsConstructor
public class AssemblerController {

    private final ImageAssemblerService imageAssemblerService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/api/image/assembler")
    public void addUserRequestToQueue(@Payload ImageRequest request, @Header("simpSessionId") String sessionId) {
        request.setSessionId(sessionId);
        imageAssemblerService.addToQueue(request);
    }

    @EventListener
    public void sendImageInfoToUser(ImageResponse response) {
        messagingTemplate.convertAndSend("/assembler/result/" + response.getSessionId(), response);
    }

    @EventListener
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        imageAssemblerService.removeFromQueue(sha.getSessionId());
    }

}
