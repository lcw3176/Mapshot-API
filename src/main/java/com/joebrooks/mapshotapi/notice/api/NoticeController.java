package com.joebrooks.mapshotapi.notice.api;

import com.joebrooks.mapshotapi.notice.model.PostDetailResponse;
import com.joebrooks.mapshotapi.notice.model.PostPageResponse;
import com.joebrooks.mapshotapi.repository.notice.NoticeEntity;
import com.joebrooks.mapshotapi.repository.notice.NoticeService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
@CrossOrigin("*")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/{contentNumber}")
    public ResponseEntity<List<PostPageResponse>> showNoticeList(
            @Positive @PathVariable(value = "contentNumber") long requestPage) {

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

    @GetMapping("/content/{contentNumber}")
    public ResponseEntity<PostDetailResponse> showPost(
            @Positive @PathVariable(value = "contentNumber") long postNumber) {

        NoticeEntity noticeEntity = noticeService.getPost(postNumber);

        return ResponseEntity.ok(
                PostDetailResponse.builder()
                        .id(noticeEntity.getId())
                        .noticeType(noticeEntity.getNoticeType())
                        .createdDate(noticeEntity.getCreatedDate())
                        .title(noticeEntity.getTitle())
                        .content(noticeEntity.getContent())
                        .build());
    }
}
