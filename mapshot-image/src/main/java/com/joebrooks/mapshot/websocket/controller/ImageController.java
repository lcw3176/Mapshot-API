package com.joebrooks.mapshot.websocket.controller;

import com.joebrooks.mapshot.assembler.ImageAssemblerWaitQueue;
import com.joebrooks.mapshot.websocket.model.ImageRequest;
import com.joebrooks.mapshot.websocket.model.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ImageAssemblerWaitQueue imageAssemblerWaitQueue;

    @MessageMapping("/api/image/assembler/register")
    public void addUserRequestToQueue(@Payload ImageRequest request, @Header("simpSessionId") String sessionId) {
        request.setSessionId(sessionId);
        imageAssemblerWaitQueue.add(request);
    }


    @EventListener
    public void sendImageInfoToUser(ImageResponse response) {
        messagingTemplate.convertAndSend("/assembler/result/" + response.getSessionId(), response);
    }
}
