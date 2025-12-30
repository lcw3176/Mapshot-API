package com.mapshot.api.infra.webLog;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RequestWrapperTest {

    @Test
    void RequestWrapper_생성_성공() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test");
        request.setContent("test content".getBytes());

        // when
        RequestWrapper wrapper = new RequestWrapper(request);

        // then
        assertThat(wrapper).isNotNull();
    }

    @Test
    void InputStream_읽기_성공() throws Exception {
        // given
        String content = "test content";
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test");
        request.setContent(content.getBytes());

        RequestWrapper wrapper = new RequestWrapper(request);

        // when
        ServletInputStream inputStream = wrapper.getInputStream();
        byte[] buffer = new byte[content.length()];
        int bytesRead = inputStream.read(buffer);

        // then
        assertThat(bytesRead).isGreaterThan(0);
        assertThat(new String(buffer, 0, bytesRead)).isEqualTo(content);
    }

    @Test
    void InputStream_여러번_읽기_가능() throws Exception {
        // given
        String content = "test content";
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test");
        request.setContent(content.getBytes());

        RequestWrapper wrapper = new RequestWrapper(request);

        // when
        ServletInputStream inputStream1 = wrapper.getInputStream();
        ServletInputStream inputStream2 = wrapper.getInputStream();

        // then
        assertThat(inputStream1).isNotNull();
        assertThat(inputStream2).isNotNull();
    }

    @Test
    void InputStream_isFinished_테스트() throws Exception {
        // given
        String content = "test";
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test");
        request.setContent(content.getBytes());

        RequestWrapper wrapper = new RequestWrapper(request);
        ServletInputStream inputStream = wrapper.getInputStream();

        // when
        while (inputStream.read() != -1) {
            // 읽기
        }

        // then
        assertTrue(inputStream.isFinished());
    }

    @Test
    void InputStream_isReady_테스트() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test");
        request.setContent("test".getBytes());

        RequestWrapper wrapper = new RequestWrapper(request);
        ServletInputStream inputStream = wrapper.getInputStream();

        // then
        assertTrue(inputStream.isReady());
    }

    @Test
    void InputStream_setReadListener_예외_발생() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test");
        request.setContent("test".getBytes());

        RequestWrapper wrapper = new RequestWrapper(request);
        ServletInputStream inputStream = wrapper.getInputStream();
        ReadListener readListener = mock(ReadListener.class);

        // when & then
        assertThrows(UnsupportedOperationException.class, () -> {
            inputStream.setReadListener(readListener);
        });
    }
}

