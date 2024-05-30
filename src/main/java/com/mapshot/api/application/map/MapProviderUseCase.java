package com.mapshot.api.application.map;

import com.mapshot.api.domain.map.provider.MapStorageService;
import com.mapshot.api.domain.map.provider.model.StorageInner;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MapProviderUseCase {

    private final MapStorageService mapStorageService;

    public void save(String uuid, String encodedImage) {
        mapStorageService.saveWhileOneMinute(uuid, encodedImage);
    }

    public byte[] getImage(String uuid) {
        return mapStorageService.pop(uuid);
    }


    @Scheduled(cron = "0/30 * * * * *")
    public void clean() {
        LocalDateTime nowTime = LocalDateTime.now();

        for (StorageInner i : mapStorageService.getAll()) {
            if (i.getCreatedAt().plusSeconds(60).isBefore(nowTime)) {
                mapStorageService.remove(i.getUuid());
            }
        }
    }
}
