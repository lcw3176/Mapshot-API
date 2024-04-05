package com.mapshot.api.domain.admin;

import com.mapshot.api.domain.community.post.PostRepository;
import com.mapshot.api.domain.notice.NoticeEntity;
import com.mapshot.api.domain.notice.NoticeRepository;
import com.mapshot.api.domain.notice.NoticeType;
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
public class AdminService {

    private final AdminRepository adminRepository;
    private final PostRepository postRepository;
    private final Validation adminValidation;
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public void validateUser(String nickname, String password) {
        password = EncryptUtil.encrypt(password);

        adminRepository.findByNicknameAndPassword(nickname, password)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_USER));
    }

    @Transactional
    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public long saveNotice(NoticeType type, String title, String content) {

        return noticeRepository.save(NoticeEntity.builder()
                        .noticeType(type)
                        .title(title)
                        .content(content)
                        .build())
                .getId();
    }

    @Transactional
    public long modifyNotice(long id, NoticeType type, String title, String content) {

        NoticeEntity noticeEntity = noticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));

        noticeEntity.update(title, type, content);

        return noticeRepository.save(noticeEntity).getId();
    }


    @Transactional
    public void deleteNotice(long id) {
        NoticeEntity noticeEntity = noticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));

        noticeRepository.delete(noticeEntity);
    }


    public HttpHeaders getAuthHeader() {
        return adminValidation.makeHeader();
    }


}
