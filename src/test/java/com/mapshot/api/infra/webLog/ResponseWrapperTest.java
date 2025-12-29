package com.mapshot.api.infra.webLog;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseWrapperTest {

    @Test
    void ResponseWrapper_생성_성공() {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        ResponseWrapper wrapper = new ResponseWrapper(response);

        // then
        assertThat(wrapper).isNotNull();
    }

    @Test
    void ResponseWrapper_상속_확인() {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        ResponseWrapper wrapper = new ResponseWrapper(response);

        // then
        assertThat(wrapper).isInstanceOf(org.springframework.web.util.ContentCachingResponseWrapper.class);
    }
}

