package com.mapshot.api.presentation.map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class ImageBuildRequestControllerTest {
    

    @Autowired
    MockMvc mockMvc;


    @Test
    void 지도_생성_요청() throws Exception {

        MapBuildRequest request = MapBuildRequest.builder()
                .companyType("kakao")
                .lat(111)
                .layerMode(false)
                .level(2)
                .lng(11)
                .type("satellite_base")
                .noLabel(false)
                .build();


        mockMvc.perform(get("/image/generate")
                        .queryParam("companyType", request.getCompanyType().toString())
                        .queryParam("lat", Double.toString(request.getLat()))
                        .queryParam("lng", Double.toString(request.getLng()))
                        .queryParam("layerMode", Boolean.toString(request.isLayerMode()))
                        .queryParam("level", Integer.toString(request.getLevel()))
                        .queryParam("type", request.getType())
                        .queryParam("noLabel", Boolean.toString(request.isNoLabel())))
                .andExpect(status().is3xxRedirection())
                .andDo(document("image/generate",
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
                                parameterWithName("noLabel")
                                        .description("일반지도 라벨링 적용 유무"),
                                parameterWithName("level")
                                        .description("지도 캡쳐 반경. 단위는 km이며 1,2,5,10의 단계가 존재함"),
                                parameterWithName("type")
                                        .description("캡쳐할 지도 타입. 위성 지도, 일반 지도, 위성 지도에 지형지물 정보가 포함되는 하이브리도 3가지가 존재함")
                        )))
                .andReturn();

    }

}