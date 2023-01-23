package com.joebrooks.mapshot.client;

import com.joebrooks.mapshot.auth.JwtTokenProvider;
import com.joebrooks.mapshot.model.ImageRequest;
import com.joebrooks.mapshot.model.ImageResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class LambdaClient extends CommonClient {

    @Value("${lambda.host}")
    private String host;

    @Value("${lambda.path}")
    private String path;

    public List<ImageResponse> sendRequest(ImageRequest request) {
        int timeoutMillis = 30 * 1000;

        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(host)
                .path(path)
                .queryParam("type", request.getType())
                .queryParam("companyType", request.getCompanyType())
                .queryParam("lat", request.getLat())
                .queryParam("lng", request.getLng())
                .queryParam("level", request.getLevel())
                .queryParam("layerMode", request.isLayerMode())
                .queryParam(JwtTokenProvider.HEADER_NAME, JwtTokenProvider.generate())
                .build()
                .toString();

        return List.of(get(url, timeoutMillis, ImageResponse[].class));
    }
}
