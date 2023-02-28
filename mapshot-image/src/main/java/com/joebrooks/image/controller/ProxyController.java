package com.joebrooks.image.controller;

import com.joebrooks.image.client.LambdaClient;
import com.joebrooks.image.model.ImageRequest;
import com.joebrooks.image.model.ImageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image/queue")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
public class ProxyController {

    private final LambdaClient lambdaClient;

    @GetMapping
    public ResponseEntity<List<ImageResponse>> sendUserRequestToLambda(@ModelAttribute ImageRequest mapRequest) {

        List<ImageResponse> response = lambdaClient.sendRequest(mapRequest);

        return ResponseEntity.ok(response);
    }

}
