package com.mapshot.api.domain.map.provider;


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

    private static final Map<String, MapImage> map = new ConcurrentHashMap<>();

    public void saveWhileOneMinute(String uuid, String encodedImage) {
        MapImage mapImage = MapImage.builder()
                .uuid(uuid)
                .imageByte(Base64.getDecoder().decode(encodedImage))
                .createdAt(LocalDateTime.now())
                .build();

        map.put(mapImage.getUuid(), mapImage);
    }

    public byte[] pop(String uuid) {
        byte[] image = map.get(uuid).getImageByte();
        map.remove(uuid);

        return image;
    }

    public List<MapImage> getAll() {
        List<MapImage> temp = new LinkedList<>();

        for (String i : map.keySet()) {
            temp.add(map.get(i));
        }

        return temp;
    }

    public void remove(String uuid) {
        map.remove(uuid);
    }
}
