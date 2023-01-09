package com.joebrooks.mapshot.assembler;

import com.joebrooks.mapshot.assembler.model.ImageRequest;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageAssemblerWaitQueue {

    private final Queue<ImageRequest> taskRequestQueue = new ConcurrentLinkedQueue<>();


    public void add(ImageRequest taskRequest) {
        taskRequestQueue.add(taskRequest);
    }

    public boolean isEmpty() {
        return taskRequestQueue.isEmpty();
    }

    public ImageRequest poll() {
        return taskRequestQueue.poll();
    }
}