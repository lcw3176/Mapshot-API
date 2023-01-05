package com.joebrooks.mapshot.util;

import com.joebrooks.mapshot.model.MessageResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import lombok.experimental.UtilityClass;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@UtilityClass
public class SlackMessageFormatter {

    private JSONObject getHeader(String title) {
        JSONObject block = new JSONObject();
        block.put("type", "plain_text");
        block.put("text", title);

        JSONObject header = new JSONObject();
        header.put("type", "header");
        header.put("text", block);

        return header;
    }

    private JSONObject getSection(Map<String, Object> map) {

        JSONObject textJson = new JSONObject();

        map.forEach((key, value) -> {
            textJson.put("type", "mrkdwn");
            textJson.put("text", key + ": " + value);
        });

        JSONObject json = new JSONObject();
        json.put("type", "section");
        json.put("text", textJson);

        return json;
    }

    private JSONObject getBlock(JSONObject... jsons) {
        JSONArray arr = new JSONArray();

        for (var i : jsons) {
            arr.add(i);
        }

        JSONObject json = new JSONObject();
        json.put("blocks", arr);

        return json;
    }

    public String makeExceptionMessage(MessageResponse message) {
        JSONObject headerJson = getHeader("Error");
        Map<String, Object> map = new HashMap<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd a HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        map.put("시간", simpleDateFormat.format(new Date()));

        JSONObject sectionOneJson = getSection(map);
        map.clear();

        map.put("원인", message.getTitle());
        JSONObject sectionTwoJson = getSection(map);
        map.clear();

        map.put("에러", message.getMessage());
        JSONObject sectionThreeJson = getSection(map);

        return getBlock(headerJson, sectionOneJson, sectionTwoJson, sectionThreeJson).toJSONString();
    }


}