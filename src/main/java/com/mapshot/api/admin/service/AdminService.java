package com.mapshot.api.admin.service;

import com.mapshot.api.admin.model.AdminRequest;
import com.mapshot.api.admin.repository.AdminRepository;
import com.mapshot.api.common.exception.ApiException;
import com.mapshot.api.common.exception.status.ErrorCode;
import com.mapshot.api.common.validation.token.AdminToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminToken adminToken;
    private static final String ENCRYPT_ALGORITHM = "SHA-256";

    @Transactional(readOnly = true)
    public MultiValueMap<String, String> login(AdminRequest request) {
        String nickname = request.getNickname();
        String password = encrypt(request.getPassword());

        adminRepository.findByNicknameAndPassword(nickname, password)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_USER));

        return makeToken();
    }

    public MultiValueMap<String, String> makeToken() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(adminToken.getHeaderName(), adminToken.generate());

        return map;
    }


    private String encrypt(String text) {
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
