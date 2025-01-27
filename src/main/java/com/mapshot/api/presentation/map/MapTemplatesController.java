package com.mapshot.api.presentation.map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapTemplatesController {

    @GetMapping("/google")
    public String googleTemplate(){
        return "google";
    }

    @GetMapping("/kakao")
    public String kakaoTemplate(){
        return "kakao";
    }

    @GetMapping("/naver")
    public String naverTemplate(){
        return "naver";
    }

    @GetMapping("/layer")
    public String layerTemplate(){
        return "layer";
    }
}
