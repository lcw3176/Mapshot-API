package com.joebrooks.mapshotapi.notice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joebrooks.mapshotapi.repository.notice.NoticeEntity;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@SpringBootTest
@AutoConfigureMockMvc
class NoticeControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    private static final String BASE_URL = "/notice";

    @Test
    void 게시글_목록_조회_테스트() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL + "/100"))
                .andExpect(status().isOk())
                .andReturn();

        List<NoticeEntity> actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<NoticeEntity>>() {
                });

        assertThat(actual)
                .hasSize(10)
                .isSortedAccordingTo(Comparator.comparing(NoticeEntity::getId).reversed());
    }
}