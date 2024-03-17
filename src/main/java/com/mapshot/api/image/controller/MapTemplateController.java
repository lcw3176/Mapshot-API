package com.mapshot.api.image.controller;

import com.mapshot.api.infra.client.lambda.LambdaRequest;
import com.mapshot.api.infra.web.auth.annotation.PreAuth;
import com.mapshot.api.infra.web.auth.enums.Accessible;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/image/template")
public class MapTemplateController {

    @PreAuth(Accessible.FRIENDLY_SERVER)
    @GetMapping("/kakao")
    public String getKakaoMap(@ModelAttribute LambdaRequest mapRequest, Model model) {
        model.addAttribute("mapRequest", mapRequest);

        return "map/kakao";
    }

    @PreAuth(Accessible.FRIENDLY_SERVER)
    @GetMapping("/google")
    public String getGoogleMap(@ModelAttribute LambdaRequest mapRequest, Model model) {
        model.addAttribute("mapRequest", mapRequest);

        return "map/google";
    }
}