package com.joebrooks.mapshot.assembler.service;

import com.joebrooks.mapshot.assembler.model.ImageRequest;
import com.joebrooks.mapshot.assembler.model.ImageResponse;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageAssemblerService {

    private final Queue<ImageRequest> imageRequestQueue = new ConcurrentLinkedQueue<>();
    private final SimpMessagingTemplate messagingTemplate;

    public void addToQueue(ImageRequest taskRequest) {
        imageRequestQueue.add(taskRequest);
    }

    public boolean isQueueEmpty() {
        return imageRequestQueue.isEmpty();
    }

    public ImageRequest pollFromQueue() {
        return imageRequestQueue.poll();
    }

    public void sendImageInfoToUser(ImageResponse response) {
        messagingTemplate.convertAndSend("/assembler/result/" + response.getSessionId(), response);
    }
}
