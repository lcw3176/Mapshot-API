package com.joebrooks.mapshotapi.notice.controller;

import com.joebrooks.mapshotapi.notice.model.PostDetailResponse;
import com.joebrooks.mapshotapi.notice.model.PostPageResponse;
import com.joebrooks.mapshotapi.repository.notice.NoticeEntity;
import com.joebrooks.mapshotapi.repository.notice.NoticeService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/{page}")
    public ResponseEntity<List<PostPageResponse>> showNoticeList(
            @Positive @PathVariable(value = "page") int requestPage) {

        List<NoticeEntity> pages = noticeService.getPosts(requestPage);
        List<PostPageResponse> postPageResponses = new ArrayList<>();

        for (NoticeEntity page : pages) {
            postPageResponses.add(PostPageResponse.builder()
                    .id(page.getId())
                    .title(page.getTitle())
                    .noticeType(page.getNoticeType())
                    .createdDate(page.getCreatedDate())
                    .build());
        }

        return ResponseEntity.ok(postPageResponses);
    }

    @GetMapping
    public ResponseEntity<PostDetailResponse> showPost(
            @Positive @RequestParam(name = "post", required = false) Long postNumber) {

        NoticeEntity noticeEntity = noticeService.getPost(postNumber);

        return ResponseEntity.ok(
                PostDetailResponse.builder()
                        .noticeType(noticeEntity.getNoticeType().getTitle())
                        .createdDate(noticeEntity.getCreatedDate())
                        .title(noticeEntity.getTitle())
                        .content(noticeEntity.getContent())
                        .build());
    }
}
