package com.mapshot.api.presentation.map.builder;

import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/image/template")
public class TemplateProviderController {

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/kakao")
    public String getKakaoMap(@ModelAttribute MapBuildRequest mapRequest, Model model) {
        model.addAttribute("mapRequest", mapRequest);

        return "map/kakao";
    }

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/google")
    public String getGoogleMap(@ModelAttribute MapBuildRequest mapRequest, Model model) {
        model.addAttribute("mapRequest", mapRequest);

        return "map/google";
    }
}
