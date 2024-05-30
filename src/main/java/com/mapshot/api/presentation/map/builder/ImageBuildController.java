package com.mapshot.api.presentation.map.builder;

import com.mapshot.api.application.map.MapBuilderUseCase;
import com.mapshot.api.domain.map.builder.MapBuildResponse;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image/queue")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
public class ImageBuildController {

    private final MapBuilderUseCase mapBuilderUseCase;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping
    public ResponseEntity<List<MapBuildResponse>> sendUserRequestToLambda(@ModelAttribute MapBuildRequest request) {
        List<MapBuildResponse> response = mapBuilderUseCase.makeOrderToMapImage(request.toQueryParams());

        return ResponseEntity.ok(response);
    }

}
