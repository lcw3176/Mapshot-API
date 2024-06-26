package com.mapshot.api.infra.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HtmlTagUtilTest {

    @Test
    void 문자열을_html_태그로_감싼다() {
        String target = "[차관동정] 국토교통부, 고흥·울진·안동 신규 국가산단 예타 본격 추진";
        String result = HtmlTagUtil.wrapHtmlTag(target, "p");
        String expected = "<p>[차관동정] 국토교통부, 고흥·울진·안동 신규 국가산단 예타 본격 추진</p>";

        assertEquals(result, expected);
    }

    @Test
    void 문자열을_html_태그로_감싸면서_속성값을_준다() {
        String target = "[차관동정] 국토교통부, 고흥·울진·안동 신규 국가산단 예타 본격 추진";
        String result = HtmlTagUtil.wrapHtmlTag(target, "a", Map.of("href", "http://hello.com"));
        String expected = "<a href=http://hello.com>[차관동정] 국토교통부, 고흥·울진·안동 신규 국가산단 예타 본격 추진</a>";

        assertEquals(result, expected);
    }

}