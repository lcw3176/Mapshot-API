package com.joebrooks.image.scheduler;

import com.joebrooks.image.model.StorageInner;
import com.joebrooks.image.service.StorageService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class StorageCleanerScheduler {

    private final StorageService storageService;

    @Scheduled(cron = "0 0 0 * * *")
    public void clean() {
        LocalDateTime nowTime = LocalDateTime.now();

        for (StorageInner i : storageService.getAll()) {
            if (i.getCreatedAt().plusMinutes(30).isBefore(nowTime)) {
                storageService.remove(i.getUuid());
            }
        }
    }

}