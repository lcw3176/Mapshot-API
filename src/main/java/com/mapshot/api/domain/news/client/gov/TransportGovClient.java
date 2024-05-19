package com.mapshot.api.domain.news.client.gov;

import com.mapshot.api.infra.client.ApiHandler;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransportGovClient {

    @Value("${client.gov.url}")
    private String path;

    public List<TransportGovResponse> getKeywords() {
        return ApiHandler.handle(() -> {
            List<TransportGovResponse> keywords = new ArrayList<>();

            Document doc = null;

            try {
                doc = Jsoup.connect(path).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Element table = doc.getElementsByClass("table line_no bd_tbl bd_tbl_ul").get(0);
            Element tbody = table.getElementsByTag("tbody").get(0);
            Elements tr = tbody.getElementsByTag("tr");

            for (Element i : tr) {
                LocalDate uploadDate = LocalDate.parse(i.getElementsByClass("bd_date").get(0).text());
                String title = i.getElementsByClass("bd_title").get(0).text();

                if (uploadDate.isBefore(LocalDate.now())) {
                    break;
                }

                TransportGovResponse response = TransportGovResponse.builder()
                        .uploadDate(uploadDate)
                        .keyword(title)
                        .build();

                keywords.add(response);
            }

            return keywords;
        });
    }
}
