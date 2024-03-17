package com.mapshot.api.domain.map;


import com.mapshot.api.presentation.map.model.StorageInner;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class StorageCleanerScheduler {

    private final StorageService storageService;

    @Scheduled(cron = "0 * * * * *")
    public void clean() {
        LocalDateTime nowTime = LocalDateTime.now();

        for (StorageInner i : storageService.getAll()) {
            if (i.getCreatedAt().plusMinutes(5).isBefore(nowTime)) {
                storageService.remove(i.getUuid());
            }
        }
    }

}