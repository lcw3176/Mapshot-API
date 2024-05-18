package com.mapshot.api.domain.map.builder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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


        when(lambdaClient.sendRequest(any(MultiValueMap.class))).
                thenReturn(lst);

        List<MapBuildResponse> responses = lambdaClient.sendRequest(new LinkedMultiValueMap<>());

        assertArrayEquals(responses.toArray(), lst.toArray());
    }
}