package com.mapshot.api.presentation;

import com.mapshot.api.domain.news.NewsService;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news-test")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
public class testcontroller {

    private final NewsService newsService;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping
    public ResponseEntity<Void> test() {
        newsService.updateNewsLetter();

        return ResponseEntity.ok().build();
    }
}
