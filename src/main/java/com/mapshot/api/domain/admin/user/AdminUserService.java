package com.mapshot.api.domain.admin.user;

import com.mapshot.api.infra.auth.Validation;
import com.mapshot.api.infra.encrypt.EncryptUtil;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final Validation adminValidation;

    @Transactional(readOnly = true)
    public void validateUser(String nickname, String password) {
        password = EncryptUtil.encrypt(password);

        adminUserRepository.findByNicknameAndPassword(nickname, password)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_USER));
    }


    public HttpHeaders getAuthHeader() {
        return adminValidation.makeHeader();
    }


}
