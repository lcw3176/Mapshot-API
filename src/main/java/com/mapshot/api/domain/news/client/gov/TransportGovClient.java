package com.mapshot.api.domain.news.client.gov;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransportGovClient {

    @Value("${client.gov.path}")
    private String path;

    public List<TransportGovResponse> getKeywords() {
        try {
            List<TransportGovResponse> keywords = new ArrayList<>();

            Document doc = Jsoup.connect(path).get();

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

        } catch (Exception e) {
            throw new ApiException(ErrorCode.GOV_CRAWLING_FAILED);
        }

    }
}
