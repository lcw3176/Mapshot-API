package com.joebrooks.mapshot.waitlist.controller;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joebrooks.mapshot.waitlist.model.WaitListResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    private WebSocketStompClient stompClient;

    @BeforeEach
    public void setup() {
        stompClient = new WebSocketStompClient(new SockJsClient(
                asList(new WebSocketTransport(new StandardWebSocketClient()))));
    }

    @Test
    void 아무도_대기열에_없을_경우_0번_인덱스를_발급받는다() throws Exception {
        SessionMaker sessionMaker = new SessionMaker();

        assertEquals(0, sessionMaker.getHandler().getResponse().getIndex());

        sessionMaker.getSession().disconnect();
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 5, 10, 25})
    void 다른_유저들이_대기중이면_해당되는_대기번호를_발급받는다(int myIndex) throws Exception {
        List<StompSession> sessions = new ArrayList<>();

        for (int i = 0; i < myIndex; i++) {
            SessionMaker session = new SessionMaker();

            assertEquals(i, session.getHandler().getResponse().getIndex());

            sessions.add(session.getSession());
        }

        SessionMaker sessionMaker = new SessionMaker();

        assertEquals(myIndex, sessionMaker.getHandler().getResponse().getIndex());
        sessionMaker.getSession().disconnect();

        for (StompSession session : sessions) {
            session.disconnect();
        }
    }


    @Test
    void 여러_유저의_연결_및_해제_테스트() throws Exception {
        List<StompSession> sessions = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            SessionMaker session = new SessionMaker();

            assertEquals(i, session.getHandler().getResponse().getIndex());
            sessions.add(session.getSession());
        }

        for (StompSession i : sessions) {
            i.disconnect();
        }
    }


    class SessionMaker {

        private final DefaultStompFrameHandler handler;
        private final StompSession session;

        public SessionMaker() throws ExecutionException, InterruptedException, TimeoutException {
            session = stompClient
                    .connect(getWsPath(), new StompSessionHandlerAdapter() {
                    })
                    .get(1, SECONDS);
            handler = new DefaultStompFrameHandler();
            session.subscribe(WEBSOCKET_SUBSCRIBE, handler);
            session.send(WEBSOCKET_SEND, "".getBytes());
        }

        public StompSession getSession() {
            return this.session;
        }

        public DefaultStompFrameHandler getHandler() {
            return this.handler;
        }
    }

    class DefaultStompFrameHandler implements StompFrameHandler {
        private BlockingQueue<WaitListResponse> blockingQueue;

        public DefaultStompFrameHandler() {
            blockingQueue = new LinkedBlockingDeque<>();
        }

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            try {
                blockingQueue.offer(mapper.readValue(new String((byte[]) o), WaitListResponse.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        public WaitListResponse getResponse() throws InterruptedException {
            return blockingQueue.poll(1, SECONDS);
        }
    }

    private String getWsPath() {
        return String.format("ws://localhost:%d/api/image/websocket", port);
    }
}