package com.joebrooks.mapshot.generator.config;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.ScreenshotType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebdriverConfig {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;

    @Bean
    public Page chromiumPage() {
        return Playwright.create()
                .chromium()
                .launch(new LaunchOptions().setHeadless(true))
                .newContext(new Browser.NewContextOptions().setViewportSize(WIDTH, HEIGHT).setLocale("ko-KR"))
                .newPage();
    }


    @Bean
    public ScreenshotOptions screenshotOptions() {
        ScreenshotOptions options = new ScreenshotOptions();
        options.quality = 100;
        options.type = ScreenshotType.JPEG;

        return options;
    }
}
