package com.joebrooks.mapshot.storage.service;


import com.joebrooks.mapshot.storage.model.Storage;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final Map<String, Storage> map = new ConcurrentHashMap<>();

    public void add(Storage storage) {
        map.put(storage.getUuid(), storage);
    }

    public ByteArrayResource getImage(String uuid) {
        ByteArrayResource image = new ByteArrayResource(map.get(uuid).getImageByte());
        map.remove(uuid);

        return image;
    }

    public List<Storage> getAll() {
        List<Storage> temp = new LinkedList<>();

        for (String i : map.keySet()) {
            temp.add(map.get(i));
        }

        return temp;
    }

    public void remove(String uuid) {
        map.remove(uuid);
    }
}
