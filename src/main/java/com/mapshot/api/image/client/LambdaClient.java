package com.mapshot.api.image.client;


import com.mapshot.api.auth.validation.Validation;
import com.mapshot.api.common.client.CommonClient;
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

    private final Validation serverValidation;

    @Value("${lambda.host}")
    private String host;

    @Value("${lambda.path}")
    private String path;

    @Value("${jwt.image.header}")
    private String SERVER_HEADER_NAME;


    public List<ImageResponse> sendRequest(ImageRequest request) {
        long timeoutMillis = 40 * 1000L;

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
                .queryParam(SERVER_HEADER_NAME, serverValidation.getToken())
                .build()
                .toString();

        return List.of(get(url, timeoutMillis, ImageResponse[].class));
    }
}
