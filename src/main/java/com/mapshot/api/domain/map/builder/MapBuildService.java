package com.mapshot.api.domain.map.builder;

import com.mapshot.api.infra.auth.Validation;
import com.mapshot.api.infra.client.lambda.LambdaClient;
import com.mapshot.api.presentation.map.builder.model.MapBuildRequest;
import com.mapshot.api.presentation.map.builder.model.MapBuildResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
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


    public List<MapBuildResponse> requestMapImage(MapBuildRequest request) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("type", request.getType());
        queryParams.add("companyType", request.getCompanyType());
        queryParams.add("lat", Double.toString(request.getLat()));
        queryParams.add("lng", Double.toString(request.getLng()));
        queryParams.add("level", Integer.toString(request.getLevel()));
        queryParams.add("layerMode", Boolean.toString(request.isLayerMode()));
        queryParams.add(SERVER_HEADER_NAME, serverValidation.getToken());

        return lambdaClient.sendRequest(host, path, queryParams, MapBuildResponse[].class);
    }

}
