package com.joebrooks.mapshot.waitlist.controller;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joebrooks.mapshot.waitlist.model.WaitListResponse;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WaitListControllerTest {

    @LocalServerPort
    private Integer port;

    static final String WEBSOCKET_SEND = "/api/image/wait-list";
    static final String WEBSOCKET_SUBSCRIBE = "/wait-list/result";
    private final ObjectMapper mapper = new ObjectMapper();

    BlockingQueue<WaitListResponse> blockingQueue;
    WebSocketStompClient stompClient;

    @BeforeEach
    public void setup() {
        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient(
                asList(new WebSocketTransport(new StandardWebSocketClient()))));
    }

    @Test
    void 대기열_목록에_등록하고_자신의_대기순번을_받아온다() throws Exception {
        StompSession session = stompClient
                .connect(getWsPath(), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe(WEBSOCKET_SUBSCRIBE, new DefaultStompFrameHandler());
        session.send(WEBSOCKET_SEND, "".getBytes());

        assertEquals(0, blockingQueue.poll(1, SECONDS).getIndex());
    }

    class DefaultStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {

            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            try {
                Map<String, Object> tempMap = mapper.readValue(((byte[]) o), Map.class);
                WaitListResponse response =
                        mapper.convertValue(tempMap.get("body"), WaitListResponse.class);

                blockingQueue.offer(response);

            } catch (Exception e) {

            }
        }
    }

    private String getWsPath() {
        return String.format("ws://localhost:%d/api/image/websocket", port);
    }
}