package com.mapshot.api.domain.map.builder;

import com.mapshot.api.infra.client.CommonClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class LambdaClientTest {

    @Autowired
    private LambdaClient lambdaClient;

    @MockBean
    private CommonClient commonClient;

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


        when(commonClient.get(any(String.class), any(long.class), any(Class.class), any(Consumer.class))).
                thenReturn(lst.toArray(MapBuildResponse[]::new));

        List<MapBuildResponse> responses = lambdaClient.sendRequest(new LinkedMultiValueMap<>());
        
        assertArrayEquals(responses.toArray(), lst.toArray());
    }
}