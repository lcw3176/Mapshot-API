package com.mapshot.api.presentation.admin;

import com.mapshot.api.domain.news.NewsService;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/news")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
@Validated
public class AdminNewsController {

    private final NewsService newsService;


    @PreAuth(Accessible.ADMIN)
    @GetMapping("/update")
    public ResponseEntity<Void> updateNewsLetter() {
        newsService.updateNewsLetter();

        return ResponseEntity.ok().build();
    }
}
