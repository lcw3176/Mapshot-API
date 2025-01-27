package com.mapshot.api.presentation.map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapTemplatesController {

    @GetMapping("/google")
    public String googleTemplate(Model model){
        return "google";
    }
}
