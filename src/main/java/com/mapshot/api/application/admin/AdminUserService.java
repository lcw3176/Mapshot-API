package com.mapshot.api.application.admin;

import com.mapshot.api.domain.admin.AdminUserRepository;
import com.mapshot.api.domain.notice.Notice;
import com.mapshot.api.domain.notice.NoticeRepository;
import com.mapshot.api.domain.notice.NoticeType;
import com.mapshot.api.infra.exception.ApiException;
import com.mapshot.api.infra.exception.status.ErrorCode;
import com.mapshot.api.infra.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final NoticeRepository noticeRepository;



    public void login(String nickname, String password) {
        password = EncryptUtil.encrypt(password);

        adminUserRepository.findByUserNameAndPassword(nickname, password)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_USER));

    }

    public long saveNotice(NoticeType type, String title, String content) {
        return noticeRepository.save(Notice.builder()
                        .noticeType(type)
                        .title(title)
                        .content(content)
                        .build())
                .getId();
    }

    public void deleteNotice(long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));

        noticeRepository.delete(notice);
    }

    public long modifyNotice(long id, NoticeType type, String title, String content) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_SUCH_NOTICE));

        notice.update(title, type, content);

        return noticeRepository.save(notice).getId();
    }


}
