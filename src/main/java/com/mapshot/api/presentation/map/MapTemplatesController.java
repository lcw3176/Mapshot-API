package com.mapshot.api.presentation.map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapTemplatesController {

    @PostMapping("/google")
    public String googleTemplate(){
        return "google";
    }

    @PostMapping("/kakao")
    public String kakaoTemplate(){
        return "kakao";
    }

    @PostMapping("/naver")
    public String naverTemplate(){
        return "naver";
    }
}
