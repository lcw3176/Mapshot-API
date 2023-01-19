package com.joebrooks.mapshot.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.joebrooks.mapshot.model.StorageInner;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class StorageServiceTest {

    private final StorageService storageService = new StorageService();

    @AfterEach
    void reset() {
        for (StorageInner i : storageService.getAll()) {
            storageService.remove(i.getUuid());
        }
    }

    @Test
    void 데이터_추가_테스트() {
        int size = 100;

        for (int i = 0; i < size; i++) {
            new Thread(() -> storageService.add(StorageInner.builder()
                    .createdAt(LocalDateTime.now())
                    .imageByte(new byte[0])
                    .uuid(UUID.randomUUID().toString())
                    .build())).start();
        }

        while (storageService.getAll().size() < size) {

        }

        assertEquals(storageService.getAll().size(), size);
    }

    @Test
    void 없는_데이터에_접근_시도시_예외() {
        assertThatThrownBy(() -> storageService.getImage("hello"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 이미지는_반환과_동시에_삭제됨() {
        String uuid = UUID.randomUUID().toString();
        byte[] imageByte = "I am Virtual Image".getBytes();

        storageService.add(StorageInner.builder()
                .createdAt(LocalDateTime.now())
                .imageByte(imageByte)
                .uuid(uuid)
                .build());
        byte[] image = storageService.getImage(uuid);

        assertEquals(image, imageByte);
        assertThatThrownBy(() -> storageService.getImage(uuid))
                .isInstanceOf(NullPointerException.class);
    }

}