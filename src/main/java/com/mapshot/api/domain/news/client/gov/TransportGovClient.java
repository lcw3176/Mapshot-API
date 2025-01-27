package com.mapshot.api.domain.news.client.gov;

import com.mapshot.api.infra.client.ApiHandler;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
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
                doc = Jsoup.connect(path)
                        .timeout(10000)
                        .sslSocketFactory(socketFactory())
                        .ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                        .followRedirects(true)
                        .get();
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

    private static SSLSocketFactory socketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to create a SSL socket factory", e);
        }
    }
}
