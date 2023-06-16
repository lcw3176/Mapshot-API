package com.mapshot.api.admin.service;

import com.mapshot.api.admin.model.AdminRequest;
import com.mapshot.api.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private static final String NO_SUCH_ALGORITHM = "[ERROR] 암호화 알고리즘 탐색 불가";
    private static final String NO_SUCH_USER = "[ERROR] Admin 유저 탐색 불가";
    private static final String ENCRYPT_ALGORITHM = "SHA-256";

    @Transactional(readOnly = true)
    public boolean login(AdminRequest request) {
        String nickname = request.getNickname();
        String password = encrypt(request.getPassword());

        adminRepository.findByNicknameAndPassword(nickname, password)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_USER));

        return true;
    }

    private String encrypt(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance(ENCRYPT_ALGORITHM);
            md.update(text.getBytes());

            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(NO_SUCH_ALGORITHM);
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
