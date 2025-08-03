package com.mapshot.api.application.admin;

import com.mapshot.api.domain.admin.user.AdminUserService;
import com.mapshot.api.domain.community.comment.CommentService;
import com.mapshot.api.domain.community.post.PostService;
import com.mapshot.api.domain.news.NewsService;
import com.mapshot.api.domain.notice.NoticeService;
import com.mapshot.api.domain.notice.NoticeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUseCase {

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

    public void forceNewsUpdate() {
        newsService.updateNews();
    }

    public void deletePost(long postId) {
        postService.deleteById(postId);
        commentService.deleteByPostId(postId);
    }
}
