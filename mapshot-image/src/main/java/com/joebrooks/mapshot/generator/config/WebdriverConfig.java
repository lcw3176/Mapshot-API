package com.joebrooks.mapshot.generator.config;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebdriverConfig {

    private static final int WIDTH = 1000;
    private static final int TIME_OUT = 40;


    @Bean
    public ChromeOptions chromeOptions() {
        String chromeDriverPath = "CHROMEDRIVER_PATH";
        String chromeBinaryPath = "CHROME_BINARY";

        ChromeOptions options = new ChromeOptions();

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            System.setProperty("webdriver.chrome.driver", "D:/chromedriver.exe");
        } else {
            System.setProperty("webdriver.chrome.driver", System.getenv(chromeDriverPath));
            options.setBinary(System.getenv(chromeBinaryPath));
        }

        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--hide-scrollbars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--incognito");

        return options;
    }

    @Bean(destroyMethod = "quit")
    public ChromeDriverExtends chromeDriverExtends() throws Exception {
        ChromeDriverExtends chromeDriverExtends = new ChromeDriverExtends(chromeOptions());
        chromeDriverExtends.manage().window().setSize(new Dimension(WIDTH, WIDTH));

        return chromeDriverExtends;
    }

    @Bean
    public WebDriverWait webDriverWait() throws Exception {
        return new WebDriverWait(chromeDriverExtends(), TIME_OUT);
    }
}
