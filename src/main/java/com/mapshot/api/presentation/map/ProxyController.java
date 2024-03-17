package com.mapshot.api.presentation.map;

import com.mapshot.api.infra.client.lambda.LambdaImageClient;
import com.mapshot.api.infra.client.lambda.model.LambdaRequest;
import com.mapshot.api.infra.client.lambda.model.LambdaResponse;
import com.mapshot.api.infra.web.auth.annotation.PreAuth;
import com.mapshot.api.infra.web.auth.enums.Accessible;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image/queue")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
public class ProxyController {

    private final LambdaImageClient lambdaImageClient;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping
    public ResponseEntity<List<LambdaResponse>> sendUserRequestToLambda(@ModelAttribute LambdaRequest mapRequest) {

        List<LambdaResponse> response = lambdaImageClient.sendRequest(mapRequest);

        return ResponseEntity.ok(response);
    }

}
