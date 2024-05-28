package com.mapshot.api.infra.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

public class HtmlTagUtil {

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


    public static String getMetaTagImage(String url) {
        Document document = null;

        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Elements meta = document.getElementsByTag("meta");

        for (Element i : meta) {
            String prop = i.attr("property");

            if (prop.equals("og:image")) {
                return i.attr("content");
            }
        }

        return "";
    }

}
