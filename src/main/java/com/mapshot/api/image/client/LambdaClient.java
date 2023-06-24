package com.mapshot.api.image.client;


import com.mapshot.api.common.client.CommonClient;
import com.mapshot.api.common.validation.token.ImageToken;
import com.mapshot.api.image.model.ImageRequest;
import com.mapshot.api.image.model.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LambdaClient extends CommonClient {

    @Value("${lambda.host}")
    private String host;

    @Value("${lambda.path}")
    private String path;

    private final ImageToken imageToken;

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
                .queryParam(imageToken.getHeaderName(), imageToken.generate())
                .build()
                .toString();

        return List.of(get(url, timeoutMillis, ImageResponse[].class));
    }
}
