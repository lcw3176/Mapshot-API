package com.joebrooks.mapshot.storage.controller;

import com.joebrooks.mapshot.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image/storage")
public class StorageController {

    private final StorageService storageService;

    @GetMapping("/{uuid}")
    public ResponseEntity<ByteArrayResource> findCompletedImage(@PathVariable String uuid) {
        ByteArrayResource imageResource = storageService.getImage(uuid);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageResource);
    }
}