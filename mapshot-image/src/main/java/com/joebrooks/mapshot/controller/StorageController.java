package com.joebrooks.mapshot.controller;

import com.joebrooks.mapshot.model.StorageInner;
import com.joebrooks.mapshot.model.StorageRequest;
import com.joebrooks.mapshot.service.StorageService;
import java.time.LocalDateTime;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image/storage")
@CrossOrigin(originPatterns = "https://*.kmapshot.com")
public class StorageController {

    private final StorageService storageService;

    @GetMapping("/{uuid}")
    public ResponseEntity<ByteArrayResource> returnCompletedImageToUser(@PathVariable String uuid) {
        byte[] imageResource = storageService.getImage(uuid);
        ByteArrayResource imageByte = new ByteArrayResource(imageResource);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageByte);
    }

    @PostMapping
    public void saveCompletedImage(@RequestBody StorageRequest storageRequest) {
        StorageInner storageInner = StorageInner.builder()
                .uuid(storageRequest.getUuid())
                .imageByte(Base64.getDecoder().decode(storageRequest.getBase64EncodedImage()))
                .createdAt(LocalDateTime.now())
                .build();

        storageService.add(storageInner);
    }
}