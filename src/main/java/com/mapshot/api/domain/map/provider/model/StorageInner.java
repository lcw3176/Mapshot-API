package com.mapshot.api.domain.map.provider.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorageInner {

    private String uuid;
    private byte[] imageByte;
    private LocalDateTime createdAt;
}
