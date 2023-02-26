package com.joebrooks.image.client;

import com.joebrooks.common.auth.JwtTokenProvider;
import com.joebrooks.common.client.CommonClient;
import com.joebrooks.image.model.ImageRequest;
import com.joebrooks.image.model.ImageResponse;
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
                .queryParam(JwtTokenProvider.HEADER_NAME, JwtTokenProvider.generate())
                .build()
                .toString();

        return List.of(get(url, timeoutMillis, ImageResponse[].class));
    }
}
