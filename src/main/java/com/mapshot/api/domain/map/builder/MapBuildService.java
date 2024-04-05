package com.mapshot.api.domain.map.builder;

import com.mapshot.api.infra.auth.Validation;
import com.mapshot.api.infra.client.lambda.LambdaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapBuildService {

    private final Validation serverValidation;

    private final LambdaClient lambdaClient;

    @Value("${lambda.host}")
    private String host;

    @Value("${lambda.path}")
    private String path;

    @Value("${jwt.image.header}")
    private String SERVER_HEADER_NAME;


    public List<MapBuildResponse> requestMapImage(MultiValueMap<String, String> queryParams) {
        queryParams.add(SERVER_HEADER_NAME, serverValidation.makeToken());

        return lambdaClient.sendRequest(host, path, queryParams, MapBuildResponse[].class);
    }

}
