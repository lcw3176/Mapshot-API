package com.mapshot.api.presentation.map;

import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/image/template")
@CrossOrigin("*")
public class TemplateProviderController {

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/naver")
    public String getNaverMap(@ModelAttribute NaverMapRequest mapRequest, Model model) {
        model.addAttribute("mapRequest", mapRequest);

        return "map/naver";
    }

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/kakao")
    public String getKakaoMap(@ModelAttribute KakaoMapRequest mapRequest, Model model) {
        model.addAttribute("mapRequest", mapRequest);

        return "map/kakao";
    }

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/google")
    public String getGoogleMap(@ModelAttribute GoogleMapRequest mapRequest, Model model) {
        model.addAttribute("mapRequest", mapRequest);

        return "map/google";
    }
}
