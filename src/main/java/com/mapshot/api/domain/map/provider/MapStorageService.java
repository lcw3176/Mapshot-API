package com.mapshot.api.domain.map.provider;


import com.mapshot.api.domain.map.provider.model.StorageInner;
import com.mapshot.api.presentation.map.provider.model.StorageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MapStorageService {

    private final Map<String, StorageInner> map = new ConcurrentHashMap<>();

    public void add(StorageRequest request) {
        StorageInner storageInner = StorageInner.builder()
                .uuid(request.getUuid())
                .imageByte(Base64.getDecoder().decode(request.getBase64EncodedImage()))
                .createdAt(LocalDateTime.now())
                .build();

        map.put(storageInner.getUuid(), storageInner);
    }

    public byte[] getImage(String uuid) {
        byte[] image = map.get(uuid).getImageByte();
        map.remove(uuid);

        return image;
    }

    public List<StorageInner> getAll() {
        List<StorageInner> temp = new LinkedList<>();

        for (String i : map.keySet()) {
            temp.add(map.get(i));
        }

        return temp;
    }

    public void remove(String uuid) {
        map.remove(uuid);
    }
}
