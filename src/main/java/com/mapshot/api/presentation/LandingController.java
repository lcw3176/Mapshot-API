package com.mapshot.api.presentation;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LandingController {

    @GetMapping("/")
    public ResponseEntity<Void> homeController() {

        return ResponseEntity.ok().build();
    }
}
