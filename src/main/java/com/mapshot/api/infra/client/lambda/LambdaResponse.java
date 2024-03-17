package com.mapshot.api.infra.client.lambda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LambdaResponse {

    private int x;
    private int y;
    private String uuid;
}