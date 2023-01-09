package com.joebrooks.mapshot.websocket.controller;


import com.joebrooks.mapshot.websocket.model.WaitListResponse;
import com.joebrooks.mapshot.websocket.service.WaitListService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WaitListController {

    private final WaitListService waitListService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/api/image/assembler/wait-list")
    @SendTo("/assembler/wait-list")
    public ResponseEntity<WaitListResponse> addWaiters(@Header("simpSessionId") String sessionId) {
        int position = waitListService.getPosition(sessionId);

        return ResponseEntity.ok(WaitListResponse.builder()
                .index(position)
                .sessionId(sessionId)
                .build());
    }

    @EventListener
    public void noticeWaitCountToUsers(WaitListResponse waiter) {
        messagingTemplate.convertAndSend("/assembler/wait-list", waiter);
    }


}