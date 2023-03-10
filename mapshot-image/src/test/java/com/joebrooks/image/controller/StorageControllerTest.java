package com.joebrooks.image.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joebrooks.common.auth.JwtTokenProvider;
import com.joebrooks.image.model.StorageRequest;
import com.joebrooks.image.service.StorageService;
import java.util.Base64;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.generate.RestDocumentationGenerator;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class StorageControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StorageService storageService;

    @LocalServerPort
    int port;

    private static final String BASE_URL = "/image/storage";

    private static MockHttpServletRequestBuilder getRequest(String urlTemplate, Object... urlVariables) {
        return RestDocumentationRequestBuilders.get(urlTemplate, urlVariables)
                .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
    }

    private static MockHttpServletRequestBuilder postRequest(String urlTemplate, String token, String content) {
        return RestDocumentationRequestBuilders.post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header(JwtTokenProvider.HEADER_NAME, token);
    }


    @Test
    void ?????????_??????_?????????() throws Exception {
        String content = "I am Virtual Image";
        when(storageService.getImage(any(String.class)))
                .thenReturn(content.getBytes());

        mockMvc.perform(getRequest(BASE_URL + "/{uuid}", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andDo(document("image/storage/get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("uuid")
                                        .description("???????????? ???????????? uuid")
                        )));
    }


    @Test
    void ?????????_????????????_?????????() throws Exception {
        String content = Base64.getEncoder().encodeToString("I am Virtual Image".getBytes());
        StorageRequest request = StorageRequest.builder()
                .uuid(UUID.randomUUID().toString())
                .base64EncodedImage(content)
                .build();

        String bodyContent = mapper.writeValueAsString(request);

        mockMvc.perform(postRequest(BASE_URL, JwtTokenProvider.generate(), bodyContent))
                .andExpect(status().isOk())
                .andDo(document("image/storage/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(JwtTokenProvider.HEADER_NAME).description("?????? ??? ?????? ??????")
                        ),
                        requestFields(
                                fieldWithPath("uuid")
                                        .description("????????? ???????????? uuid"),
                                fieldWithPath("base64EncodedImage")
                                        .description("???????????? ?????????")
                        )));
    }


    @Test
    void ??????_??????_?????????_????????????_??????_???_??????() throws Exception {
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
    void ????????????_??????_????????????_?????????_????????????_??????_???_??????() throws Exception {
        String content = Base64.getEncoder().encodeToString("I am Virtual Image".getBytes());
        StorageRequest request = StorageRequest.builder()
                .uuid(UUID.randomUUID().toString())
                .base64EncodedImage(content)
                .build();

        String bodyContent = mapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL).content(bodyContent)
                        .header(JwtTokenProvider.HEADER_NAME, "none"))
                .andExpect(status().is4xxClientError());
    }


}