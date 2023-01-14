package com.joebrooks.mapshot.generator.service;

import com.joebrooks.mapshot.generator.config.ChromeDriverExtends;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;

@Service
@RequiredArgsConstructor
public class ImageGeneratorService {

    private final ChromeDriverExtends chromeDriverExtends;
    private final WebDriverWait webDriverWait;

    public void loadPage(UriComponents uri) {
        chromeDriverExtends.get(uri.toString());
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("checker_true")));
    }

    public void scrollPage(int x, int y) {
        StringBuilder sb = new StringBuilder();
        sb.append("scroll(");
        sb.append(x);
        sb.append(",");
        sb.append(y);
        sb.append(")");

        chromeDriverExtends.executeScript(sb.toString());
    }

    public byte[] capturePage() {
        return chromeDriverExtends.getScreenshot();
    }

}
