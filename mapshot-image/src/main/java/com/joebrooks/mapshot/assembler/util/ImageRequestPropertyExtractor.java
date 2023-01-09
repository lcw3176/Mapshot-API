package com.joebrooks.mapshot.assembler.util;

import com.joebrooks.mapshot.websocket.model.ImageRequest;
import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@UtilityClass
public class ImageRequestPropertyExtractor {

    public UriComponents getUri(ImageRequest imageRequest) {

        return UriComponentsBuilder
                .newInstance()
                .scheme("https")
                .host("kmapshot.com")
                .path("/map/gen/" + imageRequest.getCompanyType().toString())
                .queryParam("layerMode", imageRequest.isLayerMode())
                .queryParam("lat", imageRequest.getLat())
                .queryParam("lng", imageRequest.getLng())
                .queryParam("level", imageRequest.getLevel())
                .queryParam("type", imageRequest.getType())
                .build(true);
    }


    public int getWidth(ImageRequest imageRequest) {

        return Arrays.stream(imageRequest.getCompanyType().getMapRadius())
                .filter(i -> i.getLevel() == imageRequest.getLevel())
                .findFirst()
                .orElseThrow(NoSuchElementException::new)
                .getWidth();
    }
}