package com.joebrooks.mapshot.generator.service;

import com.microsoft.playwright.Locator.WaitForOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;

@Service
@RequiredArgsConstructor
public class ImageGeneratorService {

    private final Page chromiumPage;
    private final ScreenshotOptions screenshotOptions;


    public void loadPage(UriComponents uri) {
        chromiumPage.navigate(uri.toString());
        chromiumPage.locator("#checker_true")
                .waitFor(new WaitForOptions().setState(WaitForSelectorState.ATTACHED));
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
