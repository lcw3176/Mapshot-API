package com.mapshot.api.domain.map.provider;

import com.mapshot.api.domain.map.provider.model.StorageInner;
import com.mapshot.api.presentation.map.provider.model.StorageRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MapStorageServiceTest {

    private final MapStorageService mapStorageService = new MapStorageService();
    private static final String BASE64_IMAGE = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAIAAACQd1PeAAAAEElEQVR4nGIy7foHCAAA//8CvQHAzMBtMgAAAABJRU5ErkJggg==";

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
            new Thread(() -> mapStorageService.add(StorageRequest
                    .builder()
                    .base64EncodedImage(BASE64_IMAGE)
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

        mapStorageService.add(StorageRequest.builder()
                .base64EncodedImage(BASE64_IMAGE)
                .uuid(uuid)
                .build());
        byte[] image = mapStorageService.getImage(uuid);

        assertEquals(Arrays.toString(image), Arrays.toString(Base64.getDecoder().decode(BASE64_IMAGE)));
        assertThatThrownBy(() -> mapStorageService.getImage(uuid))
                .isInstanceOf(NullPointerException.class);
    }

}