package com.mapshot.api.domain.news.client.gov;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransportGovClientTest {

    @Autowired
    private TransportGovClient govClient;

    @Test
    void 오늘자_키워드_추출() {
        List<TransportGovResponse> keywords = govClient.getKeywords();

        for (TransportGovResponse i : keywords) {
            assertEquals(i.getUploadDate(), LocalDate.now());
//            System.out.println(i.getKeyword());
        }
    }
}