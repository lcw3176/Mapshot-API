package com.joebrooks.mapshot.assembler;


import com.joebrooks.mapshot.assembler.util.ImageRequestPropertyExtractor;
import com.joebrooks.mapshot.client.SlackClient;
import com.joebrooks.mapshot.generator.service.CaptureService;
import com.joebrooks.mapshot.storage.model.Storage;
import com.joebrooks.mapshot.storage.service.StorageService;
import com.joebrooks.mapshot.websocket.model.ImageRequest;
import com.joebrooks.mapshot.websocket.model.ImageResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageAssembler {


    private final CaptureService captureService;
    private final SlackClient slackClient;
    private final ImageAssemblerWaitQueue imageAssemblerWaitQueue;
    private final StorageService storageService;
    private final ApplicationEventPublisher eventPublisher;

    private static final int DIVIDED_WIDTH = 1000;


    @Scheduled(fixedDelay = 1000)
    public void execute() {

        if (!imageAssemblerWaitQueue.isEmpty()) {
            ImageRequest request = imageAssemblerWaitQueue.poll();

            try {
                UriComponents uri = ImageRequestPropertyExtractor.getUri(request);
                captureService.loadPage(uri);
                int width = ImageRequestPropertyExtractor.getWidth(request);

                for (int y = 0; y < width; y += DIVIDED_WIDTH) {
                    for (int x = 0; x < width; x += DIVIDED_WIDTH) {

                        captureService.scrollPage(x, y);
                        byte[] imageByte = captureService.capturePage();

                        String uuid = UUID.randomUUID().toString();

                        storageService.add(Storage.builder()
                                .createdAt(LocalDateTime.now())
                                .uuid(uuid)
                                .imageByte(imageByte)
                                .build());

                        eventPublisher.publishEvent(ImageResponse.builder()
                                .sessionId(request.getSessionId())
                                .uuid(uuid)
                                .error(false)
                                .x(x)
                                .y(y)
                                .build());

                    }
                }

            } catch (Exception e) {
                eventPublisher.publishEvent(ImageResponse.builder()
                        .sessionId(request.getSessionId())
                        .error(true)
                        .build());

                log.error(e.getMessage(), e);
                slackClient.sendMessage(e);

            }
        }


    }
}