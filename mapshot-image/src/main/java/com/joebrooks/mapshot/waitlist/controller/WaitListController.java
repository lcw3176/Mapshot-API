package com.joebrooks.mapshot.waitlist.controller;


import com.joebrooks.mapshot.waitlist.model.WaitListResponse;
import com.joebrooks.mapshot.waitlist.service.WaitListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WaitListController {

    private final WaitListService waitListService;

    @MessageMapping("/api/image/wait-list")
    @SendTo("/wait-list/result")
    public ResponseEntity<WaitListResponse> addWaiters(@Header("simpSessionId") String sessionId) {
        int position = waitListService.getPosition(sessionId);

        return ResponseEntity.ok(WaitListResponse.builder()
                .index(position)
                .sessionId(sessionId)
                .build());
    }
}