package com.mapshot.api.presentation;

import com.mapshot.api.SlackMockExtension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class LandingControllerTest extends SlackMockExtension {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 홈_페이지_요청_성공() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andDo(document("landing/home",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}

