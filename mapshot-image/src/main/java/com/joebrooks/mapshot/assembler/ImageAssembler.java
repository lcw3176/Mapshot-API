package com.joebrooks.mapshot.assembler;


import com.joebrooks.mapshot.assembler.model.ImageRequest;
import com.joebrooks.mapshot.assembler.model.ImageResponse;
import com.joebrooks.mapshot.assembler.service.ImageAssemblerService;
import com.joebrooks.mapshot.assembler.util.ImageRequestPropertyExtractor;
import com.joebrooks.mapshot.client.SlackClient;
import com.joebrooks.mapshot.generator.service.ImageGeneratorService;
import com.joebrooks.mapshot.storage.model.Storage;
import com.joebrooks.mapshot.storage.service.StorageService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageAssembler {

    private final SlackClient slackClient;
    private final ImageGeneratorService imageGeneratorService;
    private final ImageAssemblerService imageAssemblerService;
    private final StorageService storageService;

    private static final int DIVIDED_WIDTH = 1000;


    @Scheduled(fixedDelay = 1000)
    public void execute() {

        if (!imageAssemblerService.isQueueEmpty()) {
            ImageRequest request = imageAssemblerService.pollFromQueue();

            try {
                UriComponents uri = ImageRequestPropertyExtractor.getUri(request);
                imageGeneratorService.loadPage(uri);
                int width = ImageRequestPropertyExtractor.getWidth(request);

                for (int y = 0; y < width; y += DIVIDED_WIDTH) {
                    for (int x = 0; x < width; x += DIVIDED_WIDTH) {

                        imageGeneratorService.scrollPage(x, y);
                        byte[] imageByte = imageGeneratorService.capturePage();

                        String uuid = UUID.randomUUID().toString();

                        storageService.add(Storage.builder()
                                .createdAt(LocalDateTime.now())
                                .uuid(uuid)
                                .imageByte(imageByte)
                                .build());

                        imageAssemblerService.sendImageInfoToUser(
                                ImageResponse.builder()
                                        .sessionId(request.getSessionId())
                                        .uuid(uuid)
                                        .error(false)
                                        .x(x)
                                        .y(y)
                                        .build());

                    }
                }

            } catch (Exception e) {
                imageAssemblerService.sendImageInfoToUser(
                        ImageResponse.builder()
                                .sessionId(request.getSessionId())
                                .error(true)
                                .build());

                log.error(e.getMessage(), e);
                slackClient.sendMessage(e);

            }
        }


    }
}