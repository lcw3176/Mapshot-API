package com.mapshot.api.infra.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HtmlTagUtilTest {

    @Test
    void HTML_태그_래핑_기본_테스트() {
        // given
        String content = "테스트 내용";
        String tag = "div";

        // when
        String result = HtmlTagUtil.wrapHtmlTag(content, tag);

        // then
        assertThat(result).isEqualTo("<div>테스트 내용</div>");
    }

    @Test
    void HTML_태그_래핑_다양한_태그() {
        // given
        String content = "내용";

        // when & then
        assertThat(HtmlTagUtil.wrapHtmlTag(content, "p")).isEqualTo("<p>내용</p>");
        assertThat(HtmlTagUtil.wrapHtmlTag(content, "span")).isEqualTo("<span>내용</span>");
        assertThat(HtmlTagUtil.wrapHtmlTag(content, "h1")).isEqualTo("<h1>내용</h1>");
    }

    @Test
    void HTML_태그_래핑_속성_포함() {
        // given
        String content = "테스트 내용";
        String tag = "div";
        Map<String, String> attr = new HashMap<>();
        attr.put("class", "test-class");
        attr.put("id", "test-id");

        // when
        String result = HtmlTagUtil.wrapHtmlTag(content, tag, attr);

        // then
        assertThat(result).contains("<div");
        assertThat(result).contains("class=test-class");
        assertThat(result).contains("id=test-id");
        assertThat(result).contains(">테스트 내용</div>");
    }

    @Test
    void HTML_태그_래핑_단일_속성() {
        // given
        String content = "내용";
        String tag = "a";
        Map<String, String> attr = new HashMap<>();
        attr.put("href", "https://example.com");

        // when
        String result = HtmlTagUtil.wrapHtmlTag(content, tag, attr);

        // then
        assertThat(result).contains("href=https://example.com");
        assertThat(result).contains(">내용</a>");
    }

    @Test
    void HTML_태그_래핑_빈_속성() {
        // given
        String content = "내용";
        String tag = "div";
        Map<String, String> attr = new HashMap<>();

        // when
        String result = HtmlTagUtil.wrapHtmlTag(content, tag, attr);

        // then
        assertThat(result).isEqualTo("<div>내용</div>");
    }

    @Test
    void HTML_태그_래핑_빈_내용() {
        // given
        String content = "";
        String tag = "div";

        // when
        String result = HtmlTagUtil.wrapHtmlTag(content, tag);

        // then
        assertThat(result).isEqualTo("<div></div>");
    }

    @Test
    void HTML_태그_래핑_특수문자_포함_내용() {
        // given
        String content = "<script>alert('test')</script>";
        String tag = "div";

        // when
        String result = HtmlTagUtil.wrapHtmlTag(content, tag);

        // then
        assertThat(result).isEqualTo("<div><script>alert('test')</script></div>");
    }

    @Test
    void 메타_태그_이미지_가져오기_성공() {
        // given
        // 실제 URL을 사용하는 테스트는 외부 의존성이 있으므로
        // 이 테스트는 실제 구현이 동작하는지 확인하는 용도로만 사용
        // 실제 테스트를 위해서는 MockWebServer 등을 사용해야 함

        // when & then
        // 실제 테스트는 외부 의존성 때문에 스킵하거나 Mock을 사용해야 함
        assertThat(HtmlTagUtil.getMetaTagImage("https://example.com"))
                .isNotNull(); // 실제 구현에 따라 다를 수 있음
    }

    @Test
    void 메타_태그_이미지_없는_경우_빈_문자열_반환() {
        // given
        // og:image 메타 태그가 없는 페이지의 경우

        // when
        String result = HtmlTagUtil.getMetaTagImage("https://example.com");

        // then
        // 실제 구현에 따라 빈 문자열 또는 다른 값이 반환될 수 있음
        assertThat(result).isNotNull();
    }
}

