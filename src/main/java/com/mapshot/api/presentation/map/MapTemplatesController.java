package com.mapshot.api.presentation.map;

import com.mapshot.api.presentation.map.model.GoogleMapRequest;
import com.mapshot.api.presentation.map.model.KakaoMapRequest;
import com.mapshot.api.presentation.map.model.LayerMapRequest;
import com.mapshot.api.presentation.map.model.NaverMapRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapTemplatesController {

    @GetMapping("/google")
    public String googleTemplate(Model model, @ModelAttribute GoogleMapRequest mapRequest) {
        model.addAttribute("mapRequest", mapRequest);

        return "google";
    }

    @GetMapping("/kakao")
    public String kakaoTemplate(Model model, @ModelAttribute KakaoMapRequest mapRequest){
        model.addAttribute("mapRequest", mapRequest);

        return "kakao";
    }

    @GetMapping("/naver")
    public String naverTemplate(Model model, @ModelAttribute NaverMapRequest mapRequest) {
        model.addAttribute("mapRequest", mapRequest);

        return "naver";
    }

    @GetMapping("/layer")
    public String layerTemplate(Model model, @ModelAttribute LayerMapRequest mapRequest) {
        model.addAttribute("mapRequest", mapRequest);

        return "layer";
    }
}
