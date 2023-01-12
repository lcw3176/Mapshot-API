package com.joebrooks.mapshot.assembler.service;

import com.joebrooks.mapshot.assembler.model.ImageRequest;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageAssemblerService {

    private final Queue<ImageRequest> imageRequestQueue = new ConcurrentLinkedQueue<>();


    public void addToQueue(ImageRequest taskRequest) {
        imageRequestQueue.add(taskRequest);
    }

    public void removeFromQueue(String sessionId) {
        imageRequestQueue.stream()
                .filter(i -> i.getSessionId().equals(sessionId))
                .findAny()
                .ifPresent(imageRequestQueue::remove);
    }

    public boolean isQueueEmpty() {
        return imageRequestQueue.isEmpty();
    }

    public ImageRequest pollFromQueue() {
        return imageRequestQueue.poll();
    }
}
