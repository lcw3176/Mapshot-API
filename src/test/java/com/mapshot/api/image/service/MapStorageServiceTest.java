package com.mapshot.api.image.service;

import com.mapshot.api.domain.map.provider.MapStorageService;
import com.mapshot.api.domain.map.provider.model.StorageInner;
import com.mapshot.api.presentation.map.provider.model.StorageRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MapStorageServiceTest {

    private final MapStorageService mapStorageService = new MapStorageService();

    @AfterEach
    void reset() {
        for (StorageInner i : mapStorageService.getAll()) {
            mapStorageService.remove(i.getUuid());
        }
    }

    @Test
    void 데이터_추가_테스트() {
        int size = 100;

        for (int i = 0; i < size; i++) {
            new Thread(() -> mapStorageService.add(StorageRequest.builder()
                    .base64EncodedImage("sdgdsgsdg")
                    .uuid(UUID.randomUUID().toString())
                    .build())).start();
        }

        while (mapStorageService.getAll().size() < size) {

        }

        assertEquals(mapStorageService.getAll().size(), size);
    }

    @Test
    void 없는_데이터에_접근_시도시_예외() {
        assertThatThrownBy(() -> mapStorageService.getImage("hello"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 이미지는_반환과_동시에_삭제됨() {
        String uuid = UUID.randomUUID().toString();
        String imageByte = "I am Virtual Image";

        mapStorageService.add(StorageRequest.builder()
                .base64EncodedImage(imageByte)
                .uuid(uuid)
                .build());
        byte[] image = mapStorageService.getImage(uuid);

        assertEquals(image, imageByte.getBytes());
        assertThatThrownBy(() -> mapStorageService.getImage(uuid))
                .isInstanceOf(NullPointerException.class);
    }

}