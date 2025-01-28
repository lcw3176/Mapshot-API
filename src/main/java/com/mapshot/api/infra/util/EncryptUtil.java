package com.mapshot.api.infra.util;

import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import lombok.experimental.UtilityClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class EncryptUtil {

    private static final String ENCRYPT_ALGORITHM = "SHA-256";

    public String encrypt(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance(ENCRYPT_ALGORITHM);
            md.update(text.getBytes());

            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException(ErrorCode.NO_SUCH_ALGORITHM);
        }

    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}
