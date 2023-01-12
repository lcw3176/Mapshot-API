package com.joebrooks.mapshot.waitlist.controller;


import com.joebrooks.mapshot.waitlist.model.WaitListResponse;
import com.joebrooks.mapshot.waitlist.service.WaitListService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RestController
@RequiredArgsConstructor
public class WaitListController {

    private final WaitListService waitListService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/api/image/wait-list")
    @SendTo("/wait-list/result")
    public ResponseEntity<WaitListResponse> addWaiters(@Header("simpSessionId") String sessionId) {
        int position = waitListService.getPosition(sessionId);

        return ResponseEntity.ok(WaitListResponse.builder()
                .index(position)
                .sessionId(sessionId)
                .build());
    }

    private void sendWaitersCountToUser(WaitListResponse waiter) {
        messagingTemplate.convertAndSend("/wait-list/result", waiter);
    }

    @EventListener
    public void onSessionConnectedEvent(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        waitListService.addSession(sha.getSessionId());
    }

    @EventListener
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        waitListService.removeSession(sha.getSessionId());

        for (int i = 0; i < waitListService.getSessionsSize(); i++) {
            sendWaitersCountToUser(
                    WaitListResponse.builder()
                            .sessionId(waitListService.getSession(i))
                            .index(i)
                            .build());
        }
    }

}