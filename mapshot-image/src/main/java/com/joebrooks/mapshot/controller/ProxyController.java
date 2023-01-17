package com.joebrooks.mapshot.controller;

import com.joebrooks.mapshot.client.LambdaClient;
import com.joebrooks.mapshot.model.ImageRequest;
import com.joebrooks.mapshot.model.ImageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image/queue")
public class ProxyController {

    private final LambdaClient lambdaClient;

    @GetMapping
    public ResponseEntity<List<ImageResponse>> sendUserRequestToLambda(@ModelAttribute ImageRequest mapRequest) {

        List<ImageResponse> response = lambdaClient.sendRequest(mapRequest);

        return ResponseEntity.ok(response);
    }

}
