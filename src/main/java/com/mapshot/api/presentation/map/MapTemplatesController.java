package com.mapshot.api.presentation.map;

import com.mapshot.api.presentation.map.model.GoogleTemplateRequest;
import com.mapshot.api.presentation.map.model.KakaoTemplateRequest;
import com.mapshot.api.presentation.map.model.NaverTemplateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapTemplatesController {

    @PostMapping("/google")
    public String googleTemplate(Model model, @Valid @RequestBody GoogleTemplateRequest request){
        return "google";
    }

    @PostMapping("/kakao")
    public String kakaoTemplate(Model model, @Valid @RequestBody KakaoTemplateRequest request){
        return "kakao";
    }

    @PostMapping("/naver")
    public String naverTemplate(Model model, @Valid @RequestBody NaverTemplateRequest request){
        return "naver";
    }
}
