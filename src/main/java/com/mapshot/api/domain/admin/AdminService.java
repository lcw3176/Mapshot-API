package com.mapshot.api.domain.admin;

import com.mapshot.api.infra.auth.Validation;
import com.mapshot.api.infra.encrypt.EncryptUtil;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.presentation.admin.model.AdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final Validation adminValidation;


    @Transactional(readOnly = true)
    public void validateUser(AdminRequest request) {
        String nickname = request.getNickname();
        String password = EncryptUtil.encrypt(request.getPassword());

        adminRepository.findByNicknameAndPassword(nickname, password)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_USER));
    }

    public HttpHeaders getAuthHeader() {
        return adminValidation.makeHeader();
    }


}
