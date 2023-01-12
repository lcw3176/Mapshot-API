package com.joebrooks.mapshot.waitlist.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitListService {

    private final List<String> userSessionList = Collections.synchronizedList(new ArrayList<>());


    public int getPosition(String sessionId) {
        return userSessionList.indexOf(sessionId);
    }

    public void addSession(String sessionId) {
        userSessionList.add(sessionId);
    }

    public void removeSession(String sessionId) {
        userSessionList.remove(sessionId);
    }

    public int getSessionsSize() {
        return userSessionList.size();
    }

    public String getSession(int index) {
        return userSessionList.get(index);
    }

}
