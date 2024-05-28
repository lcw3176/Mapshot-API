package com.mapshot.api.infra.util;


import java.util.Map;

public class HtmlWrapper {

    public static String wrapHtmlTag(String content, String tag) {
        StringBuilder sb = new StringBuilder();

        sb.append("<");
        sb.append(tag);
        sb.append(">");
        sb.append(content);

        sb.append("</");
        sb.append(tag);
        sb.append(">");

        return sb.toString();
    }


    public static String wrapHtmlTag(String content, String tag, Map<String, String> attr) {
        StringBuilder sb = new StringBuilder();

        sb.append("<");
        sb.append(tag);

        for (String key : attr.keySet()) {
            sb.append(" ");
            sb.append(key);
            sb.append("=");
            sb.append(attr.get(key));
        }

        sb.append(">");
        sb.append(content);

        sb.append("</");
        sb.append(tag);
        sb.append(">");

        return sb.toString();
    }

}
