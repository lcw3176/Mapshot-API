package com.mapshot.api.presentation.map.builder;

import com.mapshot.api.domain.map.builder.MapBuildService;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import com.mapshot.api.presentation.map.builder.model.MapBuildRequest;
import com.mapshot.api.presentation.map.builder.model.MapBuildResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image/queue")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
public class ImageBuildController {

    private final MapBuildService mapBuildService;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping
    public ResponseEntity<List<MapBuildResponse>> sendUserRequestToLambda(@ModelAttribute MapBuildRequest mapRequest) {
        List<MapBuildResponse> response = mapBuildService.requestMapImage(mapRequest);

        return ResponseEntity.ok(response);
    }

}
