package com.joebrooks.mapshot.generator.controller;

import com.joebrooks.mapshot.websocket.model.ImageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/image/generator")
public class MapGeneratorController {

    @GetMapping("/kakao")
    public String getKakaoMap(@ModelAttribute ImageRequest mapRequest, Model model) {
        model.addAttribute("mapRequest", mapRequest);

        return "map/kakao";
    }
}