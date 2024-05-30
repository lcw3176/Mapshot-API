package com.mapshot.api.presentation.map.provider;

import com.mapshot.api.application.map.MapProviderUseCase;
import com.mapshot.api.infra.auth.annotation.PreAuth;
import com.mapshot.api.infra.auth.enums.Accessible;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image/storage")
@CrossOrigin(originPatterns = {"https://*.kmapshot.com", "https://kmapshot.com"})
public class ImageProviderController {

    private final MapProviderUseCase mapProviderUseCase;

    @PreAuth(Accessible.EVERYONE)
    @GetMapping("/{uuid}")
    public ResponseEntity<byte[]> returnCompletedImageToUser(@PathVariable String uuid) {
        byte[] imageResource = mapProviderUseCase.getImage(uuid);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageResource);
    }

    @PreAuth(Accessible.FRIENDLY_SERVER)
    @PostMapping
    public void saveCompletedImage(@RequestBody StorageRequest request) {
        mapProviderUseCase.save(request.getUuid(), request.getBase64EncodedImage());
    }
}