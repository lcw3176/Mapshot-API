package com.joebrooks.mapshot.websocket.service;

import com.joebrooks.mapshot.websocket.model.WaitListResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Service
@RequiredArgsConstructor
public class WaitListService {

    private final List<String> userSessionList = Collections.synchronizedList(new ArrayList<>());
    private final ApplicationEventPublisher eventPublisher;

    public int getPosition(String sessionId) {
        return userSessionList.indexOf(sessionId);
    }


    @EventListener
    private void onSessionConnectedEvent(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        userSessionList.add(sha.getSessionId());
    }

    @EventListener
    private void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        userSessionList.remove(sha.getSessionId());

        for (int i = 0; i < userSessionList.size(); i++) {
            eventPublisher.publishEvent(WaitListResponse.builder()
                    .sessionId(userSessionList.get(i))
                    .index(i)
                    .build());
        }
    }
}
