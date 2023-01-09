package com.joebrooks.mapshot.generator.service;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;

@Service
@RequiredArgsConstructor
public class CaptureService {

    private final Page chromiumPage;
    private final ScreenshotOptions screenshotOptions;


    public void loadPage(UriComponents uri) {
        chromiumPage.navigate(uri.toString());
        chromiumPage.waitForSelector("#checker_true");
    }

    public void scrollPage(int x, int y) {
        StringBuilder sb = new StringBuilder();
        sb.append("scroll(");
        sb.append(x);
        sb.append(",");
        sb.append(y);
        sb.append(")");

        chromiumPage.evaluate(sb.toString());
    }

    public byte[] capturePage() {
        return chromiumPage.screenshot(screenshotOptions);
    }

}
