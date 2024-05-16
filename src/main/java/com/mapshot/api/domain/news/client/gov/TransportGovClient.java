package com.mapshot.api.domain.news.client.gov;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransportGovClient {

    @Value("${client.gov.path}")
    private String path;

    public void sendRequest() {
        try {
            Document doc = Jsoup.connect(path).get();
            doc.getElementsByClass("table line_no bd_tbl bd_tbl_ul").forEach(System.out::println);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.GOV_CRAWLING_FAILED);
        }

    }
}
