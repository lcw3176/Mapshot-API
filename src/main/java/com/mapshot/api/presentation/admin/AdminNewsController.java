package com.mapshot.api.presentation.admin;

import com.mapshot.api.application.news.NewsUseCase;
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
@RequestMapping("/admin/news")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
public class AdminNewsController {

    private final NewsUseCase newsUseCase;


    @PreAuth(Accessible.ADMIN)
    @GetMapping("/update")
    public ResponseEntity<Void> updateNewsLetter() {
        newsUseCase.updateNewsLetter();

        return ResponseEntity.ok().build();
    }
}
