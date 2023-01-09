package com.joebrooks.mapshot.storage.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Storage {

    private String uuid;
    private byte[] imageByte;
    private LocalDateTime createdAt;
}
