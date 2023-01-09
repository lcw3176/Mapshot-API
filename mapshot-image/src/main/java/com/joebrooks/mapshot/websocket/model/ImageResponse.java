package com.joebrooks.mapshot.websocket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {

    private int x;
    private int y;
    private String uuid;
    private boolean error;

    @JsonIgnore
    private String sessionId;

}