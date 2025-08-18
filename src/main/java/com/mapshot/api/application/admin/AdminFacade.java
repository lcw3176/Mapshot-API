package com.mapshot.api.application.admin;

import com.mapshot.api.domain.admin.user.AdminUserService;
import com.mapshot.api.domain.community.comment.CommentService;
import com.mapshot.api.domain.community.post.PostService;
import com.mapshot.api.domain.news.NewsService;
import com.mapshot.api.domain.notice.NoticeService;
import com.mapshot.api.domain.notice.NoticeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional
public class AdminFacade {

    private final AdminUserService adminUserService;
    private final NoticeService noticeService;
    private final CommentService commentService;
    private final PostService postService;
    private final NewsService newsService;


    public Boolean login(String nickname, String password) {
        adminUserService.validationCheck(nickname, password);

        return true;
    }

    public void saveNotice(NoticeType type, String title, String content) {
        noticeService.save(type, title, content);
    }

    public void deleteNotice(long noticeId) {
        noticeService.delete(noticeId);
    }

    public void modifyNotice(long id, NoticeType type, String title, String content) {
        noticeService.update(id, type, title, content);
    }

    @Scheduled(cron = "0 0 21 * * *")
    public void updateNews() {
        String content = newsService.getNewsContent();

        if (content.isBlank()) {
            return;
        }

        String writer = "헤드샷";
        String title = "[" + LocalDate.now() + "] 오늘의 헤드라인";
        String password = UUID.randomUUID().toString();

        postService.save(writer, content, title, password);
    }

    public void deletePost(long postId) {
        postService.deleteById(postId);
        commentService.deleteByPostId(postId);
    }
}
