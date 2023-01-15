package com.joebrooks.mapshot.generator.config;

import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Map;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.http.HttpMethod;

public class ChromeDriverExtends extends ChromeDriver {

    public ChromeDriverExtends(ChromeOptions options) throws Exception {
        this(ChromeDriverService.createDefaultService(), options);
    }

    public ChromeDriverExtends(ChromeDriverService service, ChromeOptions options) throws Exception {
        super(service, options);
        CommandInfo cmd = new CommandInfo("/session/:sessionId/chromium/send_command_and_get_result", HttpMethod.POST);
        Method defineCommand = HttpCommandExecutor.class.getDeclaredMethod("defineCommand", String.class,
                CommandInfo.class);
        defineCommand.setAccessible(true);
        defineCommand.invoke(super.getCommandExecutor(), "sendCommand", cmd);
    }

    public byte[] getScreenshot() {
        Object result = sendCommand("Page.captureScreenshot", ImmutableMap.of("format", "jpeg"));
        String data = ((Map<String, String>) result).get("data");

        return Base64.getDecoder().decode(data);
    }

    protected Object sendCommand(String cmd, Object params) {
        return execute("sendCommand", ImmutableMap.of("cmd", cmd, "params", params)).getValue();
    }

}