package com.joebrooks.image.config;

import com.joebrooks.common.config.AbstractWebModuleConfiguration;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
public class ImageModuleServletConfig extends AbstractWebModuleConfiguration {

    private static final String BASE_PACKAGE = "com.joebrooks.image";

    @Override
    public WebApplicationContext getApplicationContext() {
        AnnotationConfigServletWebServerApplicationContext webApplicationContext
                = new AnnotationConfigServletWebServerApplicationContext();
        webApplicationContext.scan(BASE_PACKAGE);

        return webApplicationContext;
    }

    @Override
    protected String getServletName() {
        return "imageServlet";
    }

    @Override
    public String[] getMapping() {
        return new String[]{"/image", "/image/*"};
    }
}
