package com.mapshot.api.image.client;


import com.mapshot.api.common.client.CommonClient;
import com.mapshot.api.common.token.JwtUtil;
import com.mapshot.api.image.model.ImageRequest;
import com.mapshot.api.image.model.ImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class LambdaClient extends CommonClient {

    @Value("${lambda.host}")
    private String host;

    @Value("${lambda.path}")
    private String path;

    public List<ImageResponse> sendRequest(ImageRequest request) {
        long timeoutMillis = 30 * 1000L;

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
                .queryParam(JwtUtil.HEADER_NAME, JwtUtil.generate())
                .build()
                .toString();

        return List.of(get(url, timeoutMillis, ImageResponse[].class));
    }
}
