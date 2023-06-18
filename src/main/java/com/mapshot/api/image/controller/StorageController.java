package com.mapshot.api.image.controller;

import com.mapshot.api.common.annotation.PreAuth;
import com.mapshot.api.common.enums.Accessible;
import com.mapshot.api.image.model.StorageInner;
import com.mapshot.api.image.model.StorageRequest;
import com.mapshot.api.image.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image/storage")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
public class StorageController {

    private final StorageService storageService;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/{uuid}")
    public ResponseEntity<ByteArrayResource> returnCompletedImageToUser(@PathVariable String uuid) {
        byte[] imageResource = storageService.getImage(uuid);
        ByteArrayResource imageByte = new ByteArrayResource(imageResource);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageByte);
    }

    @PreAuth(Accessible.FRIENDLY_SERVER)
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