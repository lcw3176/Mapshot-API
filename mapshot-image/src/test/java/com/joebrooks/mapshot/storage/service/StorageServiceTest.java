package com.joebrooks.mapshot.storage.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.joebrooks.mapshot.storage.model.Storage;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class StorageServiceTest {

    private final StorageService storageService = new StorageService();

    @AfterEach
    void reset() {
        for (Storage i : storageService.getAll()) {
            storageService.remove(i.getUuid());
        }
    }

    @Test
    void 데이터_추가_테스트() {
        int size = 100;

        for (int i = 0; i < size; i++) {
            new Thread(() -> storageService.add(Storage.builder()
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

}