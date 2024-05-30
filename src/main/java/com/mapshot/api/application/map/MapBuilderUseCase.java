package com.mapshot.api.application.map;

import com.mapshot.api.domain.map.builder.MapBuildResponse;
import com.mapshot.api.domain.map.builder.MapBuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MapBuilderUseCase {

    private final MapBuildService mapBuildService;

    public List<MapBuildResponse> makeOrderToMapImage(MultiValueMap<String, String> queryParams) {
        return mapBuildService.requestMapImage(queryParams);
    }

}
