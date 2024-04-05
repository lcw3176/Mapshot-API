package com.mapshot.api.presentation.map.provider;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StorageRequest {

    private String uuid;
    private String base64EncodedImage;
}
