package com.mapshot.api.infra.client.lambda;

import com.mapshot.api.domain.map.builder.MapBuildResponse;
import com.mapshot.api.infra.client.CommonClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class LambdaClientTest {

    @Autowired
    private LambdaClient lambdaClient;

    @MockBean
    private CommonClient commonClient;

    @Value("${lambda.host}")
    private String host;

    @Value("${lambda.path}")
    private String path;


    @Test
    void 응답_형변환_테스트() {
        List<MapBuildResponse> lst = new ArrayList<>();

        for (int i = 1; i < 10; i++) {
            lst.add(MapBuildResponse.builder()
                    .x(i)
                    .y(i)
                    .uuid(UUID.randomUUID().toString())
                    .build());
        }


        when(commonClient.get(any(String.class), any(long.class), any(Class.class))).
                thenReturn(lst.toArray());

        List<MapBuildResponse> responses = lambdaClient.sendRequest(host, path, new LinkedMultiValueMap<>(), MapBuildResponse[].class);

        assertArrayEquals(responses.toArray(), lst.toArray());
    }
}