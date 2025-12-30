package com.mapshot.api.infra.util;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EncryptUtilTest {

    @Test
    void 암호화_성공() {
        // given
        String plainText = "testPassword123";

        // when
        String encrypted = EncryptUtil.encrypt(plainText);

        // then
        assertThat(encrypted).isNotNull();
        assertThat(encrypted).isNotEmpty();
        assertThat(encrypted).hasSize(64); // SHA-256 produces 64 character hex string
        assertThat(encrypted).isNotEqualTo(plainText);
    }

    @Test
    void 같은_문자열은_같은_해시값_생성() {
        // given
        String plainText = "testPassword123";

        // when
        String encrypted1 = EncryptUtil.encrypt(plainText);
        String encrypted2 = EncryptUtil.encrypt(plainText);

        // then
        assertThat(encrypted1).isEqualTo(encrypted2);
    }

    @Test
    void 다른_문자열은_다른_해시값_생성() {
        // given
        String plainText1 = "testPassword123";
        String plainText2 = "testPassword456";

        // when
        String encrypted1 = EncryptUtil.encrypt(plainText1);
        String encrypted2 = EncryptUtil.encrypt(plainText2);

        // then
        assertThat(encrypted1).isNotEqualTo(encrypted2);
    }

    @Test
    void 빈_문자열_암호화() {
        // given
        String plainText = "";

        // when
        String encrypted = EncryptUtil.encrypt(plainText);

        // then
        assertThat(encrypted).isNotNull();
        assertThat(encrypted).hasSize(64);
    }

    @Test
    void 긴_문자열_암호화() {
        // given
        String plainText = "a".repeat(1000);

        // when
        String encrypted = EncryptUtil.encrypt(plainText);

        // then
        assertThat(encrypted).isNotNull();
        assertThat(encrypted).hasSize(64);
    }

    @Test
    void 특수문자_포함_문자열_암호화() {
        // given
        String plainText = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

        // when
        String encrypted = EncryptUtil.encrypt(plainText);

        // then
        assertThat(encrypted).isNotNull();
        assertThat(encrypted).hasSize(64);
    }

    @Test
    void 한글_문자열_암호화() {
        // given
        String plainText = "한글비밀번호123";

        // when
        String encrypted = EncryptUtil.encrypt(plainText);

        // then
        assertThat(encrypted).isNotNull();
        assertThat(encrypted).hasSize(64);
    }

    @Test
    void null_문자열_암호화() {
        // given
        String plainText = null;

        // when & then
        assertThatThrownBy(() -> EncryptUtil.encrypt(plainText))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 암호화_결과는_16진수_형식() {
        // given
        String plainText = "test";

        // when
        String encrypted = EncryptUtil.encrypt(plainText);

        // then
        assertThat(encrypted).matches("^[0-9a-f]{64}$");
    }
}

