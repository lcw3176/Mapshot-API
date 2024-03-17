package com.mapshot.api.image.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.SlackMockExtension;
import com.mapshot.api.infra.client.lambda.LambdaImageClient;
import com.mapshot.api.infra.client.lambda.model.LambdaRequest;
import com.mapshot.api.infra.client.lambda.model.LambdaResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ProxyControllerTest extends SlackMockExtension {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LambdaImageClient lambdaImageClient;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void 이미지_제작_요청_테스트() throws Exception {
        List<LambdaResponse> responses = new ArrayList<>();

        for (int i = 0; i < 3000; i += 1000) {
            for (int j = 0; j < 3000; j += 1000) {
                LambdaResponse response = LambdaResponse.builder()
                        .uuid(UUID.randomUUID().toString())
                        .x(j)
                        .y(i)
                        .build();

                responses.add(response);
            }

        }

        when(lambdaImageClient.sendRequest(any(LambdaRequest.class)))
                .thenReturn(responses);
        LambdaRequest request = LambdaRequest.builder()
                .companyType("kakao")
                .lat(111)
                .layerMode(false)
                .level(2)
                .lng(11)
                .type("satellite_base")
                .build();

        MvcResult result = mockMvc.perform(get("/image/queue")
                        .queryParam("companyType", request.getCompanyType().toString())
                        .queryParam("lat", Double.toString(request.getLat()))
                        .queryParam("lng", Double.toString(request.getLng()))
                        .queryParam("layerMode", Boolean.toString(request.isLayerMode()))
                        .queryParam("level", Integer.toString(request.getLevel()))
                        .queryParam("type", request.getType()))
                .andExpect(status().isOk())
                .andDo(document("image/queue",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("companyType")
                                        .description("지도를 제공하는 회사"),
                                parameterWithName("lat")
                                        .description("위도"),
                                parameterWithName("lng")
                                        .description("경도"),
                                parameterWithName("layerMode")
                                        .description("도시계획 레이어 적용 유무"),
                                parameterWithName("level")
                                        .description("지도 캡쳐 반경. 단위는 km이며 1,2,5,10의 단계가 존재함"),
                                parameterWithName("type")
                                        .description("캡쳐할 지도 타입. 위성 지도, 일반 지도, 위성 지도에 지형지물 정보가 포함되는 하이브리도 3가지가 존재함")
                        ),
                        responseFields(
                                fieldWithPath("[].uuid")
                                        .description("제작된 위성 이미지의 uuid, 해당 값으로 자신이 제작 요청한 이미지를 발급받음"),
                                fieldWithPath("[].x")
                                        .description("분할 전송되는 이미지의 x 위치 값"),
                                fieldWithPath("[].y")
                                        .description("분할 전송되는 이미지의 y 위치 값")
                        )))
                .andReturn();

        List<LambdaResponse> lst = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LambdaResponse>>() {
                });

        assertThat(lst).hasSize(responses.size());
    }
}
