package com.joebrooks.image.service;


import com.joebrooks.image.model.StorageInner;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final Map<String, StorageInner> map = new ConcurrentHashMap<>();

    public void add(StorageInner storageInner) {
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
