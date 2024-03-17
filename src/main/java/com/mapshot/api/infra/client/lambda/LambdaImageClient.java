package com.mapshot.api.infra.client.lambda;


import com.mapshot.api.infra.client.CommonClient;
import com.mapshot.api.infra.client.lambda.model.LambdaRequest;
import com.mapshot.api.infra.client.lambda.model.LambdaResponse;
import com.mapshot.api.infra.web.auth.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LambdaImageClient extends CommonClient {

    private final Validation serverValidation;

    @Value("${lambda.host}")
    private String host;

    @Value("${lambda.path}")
    private String path;

    @Value("${jwt.image.header}")
    private String SERVER_HEADER_NAME;


    public List<LambdaResponse> sendRequest(LambdaRequest request) {
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
                .queryParam(SERVER_HEADER_NAME, serverValidation.getToken())
                .build()
                .toString();

        return List.of(get(url, timeoutMillis, LambdaResponse[].class));
    }
}
