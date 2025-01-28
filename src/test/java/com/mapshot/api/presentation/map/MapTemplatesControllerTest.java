package com.mapshot.api.presentation.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapshot.api.SlackMockExtension;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class MapTemplatesControllerTest extends SlackMockExtension {

    private static final String BASE_URL = "/map";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Bucket bucket;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void 구글_지도_요청_테스트() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/google")
                )
                .andExpect(status().isOk())
                .andDo(document("map/google",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void 카카오_지도_요청_테스트() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/kakao")
                )
                .andExpect(status().isOk())
                .andDo(document("map/kako",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void 네이버_지도_요청_테스트() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/naver")
                )
                .andExpect(status().isOk())
                .andDo(document("map/naver",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void 레이어_지도_요청_테스트() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/layer")
                )
                .andExpect(status().isOk())
                .andDo(document("map/layer",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void 요청_제한_테스트() throws Exception {

        bucket.tryConsumeAsMuchAsPossible();

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(BASE_URL + "/layer")
                )
                .andExpect(status().isTooManyRequests())
                .andDo(document("map/layer",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        bucket.reset();
    }
}
