package com.mapshot.api.image.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.SlackMockExtension;
import com.mapshot.api.domain.map.provider.MapStorageService;
import com.mapshot.api.infra.auth.Validation;
import com.mapshot.api.presentation.map.provider.model.StorageRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.generate.RestDocumentationGenerator;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Base64;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ImageProviderControllerTest extends SlackMockExtension {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MapStorageService mapStorageService;


    @Autowired
    Validation serverValidation;

    @Value("${jwt.image.header}")
    private String SERVER_HEADER_NAME;

    private static final String BASE_URL = "/image/storage";

    private MockHttpServletRequestBuilder getRequest(String urlTemplate, Object... urlVariables) {
        return RestDocumentationRequestBuilders.get(urlTemplate, urlVariables)
                .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
    }

    private MockHttpServletRequestBuilder postRequest(String urlTemplate, String token, String content) {
        return RestDocumentationRequestBuilders.post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header(SERVER_HEADER_NAME, token);
    }


    @Test
    void 이미지_발급_테스트() throws Exception {
        String content = "I am Virtual Image";
        when(mapStorageService.getImage(any(String.class)))
                .thenReturn(content.getBytes());

        mockMvc.perform(getRequest(BASE_URL + "/{uuid}", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andDo(document("image/storage/get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("uuid")
                                        .description("발급받을 이미지의 uuid")
                        )));
    }


    @Test
    void 이미지_임시저장_테스트() throws Exception {
        String content = Base64.getEncoder().encodeToString("I am Virtual Image".getBytes());
        StorageRequest request = StorageRequest.builder()
                .uuid(UUID.randomUUID().toString())
                .base64EncodedImage(content)
                .build();

        String bodyContent = mapper.writeValueAsString(request);

        mockMvc.perform(postRequest(BASE_URL, serverValidation.getToken(), bodyContent))
                .andExpect(status().isOk())
                .andDo(document("image/storage/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(SERVER_HEADER_NAME).description("서버 간 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("uuid")
                                        .description("저장할 이미지의 uuid"),
                                fieldWithPath("base64EncodedImage")
                                        .description("인코딩된 이미지")
                        )));
    }


    @Test
    void 토큰_없이_이미지_임시저장_요청_시_거절() throws Exception {
        String content = Base64.getEncoder().encodeToString("I am Virtual Image".getBytes());
        StorageRequest request = StorageRequest.builder()
                .uuid(UUID.randomUUID().toString())
                .base64EncodedImage(content)
                .build();

        String bodyContent = mapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL).content(bodyContent))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void 유효하지_않은_토큰으로_이미지_임시저장_요청_시_거절() throws Exception {
        String content = Base64.getEncoder().encodeToString("I am Virtual Image".getBytes());
        StorageRequest request = StorageRequest.builder()
                .uuid(UUID.randomUUID().toString())
                .base64EncodedImage(content)
                .build();

        String bodyContent = mapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL).content(bodyContent)
                        .header(SERVER_HEADER_NAME, "none"))
                .andExpect(status().is4xxClientError());
    }


}