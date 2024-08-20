package com.mapshot.api.domain.log;

import com.mapshot.api.infra.client.slack.SlackClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {

    private final SlackClient slackClient;

    public void write(String roll, String stackTrace) {
        log.error("{} {}", roll, stackTrace);
    }


    public void alert(String roll, String stackTrace) {
        String title = roll + " 서버 에러";
        slackClient.sendMessage(title, stackTrace);
    }

}
