package com.joebrooks.image.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joebrooks.image.client.LambdaClient;
import com.joebrooks.image.enums.CompanyType;
import com.joebrooks.image.model.ImageRequest;
import com.joebrooks.image.model.ImageResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ProxyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LambdaClient lambdaClient;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void ?????????_??????_??????_?????????() throws Exception {
        List<ImageResponse> responses = new ArrayList<>();

        for (int i = 0; i < 3000; i += 1000) {
            for (int j = 0; j < 3000; j += 1000) {
                ImageResponse response = ImageResponse.builder()
                        .uuid(UUID.randomUUID().toString())
                        .x(j)
                        .y(i)
                        .build();

                responses.add(response);
            }

        }

        when(lambdaClient.sendRequest(any(ImageRequest.class)))
                .thenReturn(responses);
        ImageRequest request = ImageRequest.builder()
                .companyType(CompanyType.kakao)
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
                        requestParameters(
                                parameterWithName("companyType")
                                        .description("????????? ???????????? ??????"),
                                parameterWithName("lat")
                                        .description("??????"),
                                parameterWithName("lng")
                                        .description("??????"),
                                parameterWithName("layerMode")
                                        .description("???????????? ????????? ?????? ??????"),
                                parameterWithName("level")
                                        .description("?????? ?????? ??????. ????????? km?????? 1,2,5,10??? ????????? ?????????"),
                                parameterWithName("type")
                                        .description("????????? ?????? ??????. ?????? ??????, ?????? ??????, ?????? ????????? ???????????? ????????? ???????????? ??????????????? 3????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("[].uuid")
                                        .description("????????? ?????? ???????????? uuid, ?????? ????????? ????????? ?????? ????????? ???????????? ????????????"),
                                fieldWithPath("[].x")
                                        .description("?????? ???????????? ???????????? x ?????? ???"),
                                fieldWithPath("[].y")
                                        .description("?????? ???????????? ???????????? y ?????? ???")
                        )))
                .andReturn();

        List<ImageResponse> lst = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<ImageResponse>>() {
                });

        assertThat(lst).hasSize(responses.size());
    }
}