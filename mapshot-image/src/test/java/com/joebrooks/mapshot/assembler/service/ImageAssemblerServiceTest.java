package com.joebrooks.mapshot.assembler.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.joebrooks.mapshot.assembler.model.ImageRequest;
import com.joebrooks.mapshot.generator.enums.CompanyType;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.jupiter.api.Test;

class ImageAssemblerServiceTest {

    private final ImageAssemblerService imageAssemblerService = new ImageAssemblerService();

    @Test
    void 이미지_요청_추가() {
        imageAssemblerService.addToQueue(ImageRequest.builder()
                .companyType(CompanyType.kakao)
                .lat(111)
                .layerMode(false)
                .level(1)
                .lng(11)
                .sessionId("hello")
                .type("hello")
                .build());

        assertEquals(false, imageAssemblerService.isQueueEmpty());
    }

    @Test
    void 이미지_요청_삭제() {
        imageAssemblerService.addToQueue(ImageRequest.builder()
                .companyType(CompanyType.kakao)
                .lat(111)
                .layerMode(false)
                .level(1)
                .lng(11)
                .sessionId("hello")
                .type("hello")
                .build());

        imageAssemblerService.removeFromQueue("hello");
        assertEquals(true, imageAssemblerService.isQueueEmpty());
    }


    @Test
    void 이미지_요청_순차적으로_가져오기() {
        for (int i = 0; i < 100; i++) {
            imageAssemblerService.addToQueue(ImageRequest.builder()
                    .companyType(CompanyType.kakao)
                    .lat(111)
                    .layerMode(false)
                    .level(1)
                    .lng(11)
                    .sessionId(Integer.toString(i))
                    .type("hello")
                    .build());
        }

        for (int i = 0; i < 100; i++) {
            ImageRequest imageRequest = imageAssemblerService.pollFromQueue();

            assertEquals(imageRequest.getSessionId(), Integer.toString(i));
        }
    }


    @Test
    void 이미지_요청_무작위_추가_후_순차적으로_가져오기() {
        Queue<String> uuids = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                String uuid = UUID.randomUUID().toString();
                imageAssemblerService.addToQueue(ImageRequest.builder()
                        .companyType(CompanyType.kakao)
                        .lat(111)
                        .layerMode(false)
                        .level(1)
                        .lng(11)
                        .sessionId(uuid)
                        .type("hello")
                        .build());

                uuids.add(uuid);
            }).start();

        }
        int count = 0;

        while (count < 100) {
            if (!imageAssemblerService.isQueueEmpty()) {
                assertEquals(imageAssemblerService.pollFromQueue().getSessionId(), uuids.poll());
                count++;
            }
        }
    }
}