package com.mapshot.api.board.content.controller;

import com.mapshot.api.auth.annotation.PreAuth;
import com.mapshot.api.auth.enums.Accessible;
import com.mapshot.api.board.IpUtil;
import com.mapshot.api.board.content.model.ContentDetailResponse;
import com.mapshot.api.board.content.model.ContentListResponse;
import com.mapshot.api.board.content.model.ContentRequest;
import com.mapshot.api.board.content.service.ContentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contact")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class ContentController {

    private final ContentService contentService;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/list/{pageNumber}")
    public ResponseEntity<List<ContentListResponse>> showContentList(
            @PositiveOrZero @PathVariable(value = "pageNumber") long pageNumber) {

        List<ContentListResponse> contentList = contentService.getContentList(pageNumber);

        return ResponseEntity.ok()
                .body(contentList);
    }

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/{id}")
    public ResponseEntity<ContentDetailResponse> showSpecificContent(
            @Positive @PathVariable(value = "id") long id) {

        ContentDetailResponse content = contentService.getSingleContent(id);

        return ResponseEntity.ok()
                .body(content);
    }

    @PreAuth(Accessible.EVERYONE)
    @PostMapping("/register")
    public ResponseEntity<Void> registerContent(@RequestBody ContentRequest content, HttpServletRequest request) {
        String ip = IpUtil.getClientIpAddress(request);
        content.setIp(ip);

        contentService.save(content);

        return ResponseEntity.ok().build();
    }
}
