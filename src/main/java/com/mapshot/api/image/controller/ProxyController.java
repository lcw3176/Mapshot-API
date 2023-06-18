package com.mapshot.api.image.controller;

import com.mapshot.api.common.annotation.PreAuth;
import com.mapshot.api.common.enums.Accessible;
import com.mapshot.api.image.client.LambdaClient;
import com.mapshot.api.image.model.ImageRequest;
import com.mapshot.api.image.model.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image/queue")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
public class ProxyController {

    private final LambdaClient lambdaClient;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping
    public ResponseEntity<List<ImageResponse>> sendUserRequestToLambda(@ModelAttribute ImageRequest mapRequest) {

        List<ImageResponse> response = lambdaClient.sendRequest(mapRequest);

        return ResponseEntity.ok(response);
    }

}
