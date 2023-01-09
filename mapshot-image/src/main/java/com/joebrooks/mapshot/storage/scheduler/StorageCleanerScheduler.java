package com.joebrooks.mapshot.storage.scheduler;

import com.joebrooks.mapshot.storage.model.Storage;
import com.joebrooks.mapshot.storage.service.StorageService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorageCleanerScheduler {

    private final StorageService storageService;

    @Scheduled(cron = "0 0 0 * * *")
    public void clean() {
        LocalDateTime nowTime = LocalDateTime.now();

        for (Storage i : storageService.getAll()) {
            if (i.getCreatedAt().plusMinutes(30).isBefore(nowTime)) {
                storageService.remove(i.getUuid());
            }
        }
    }

}