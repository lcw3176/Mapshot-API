package com.joebrooks.image;

import com.joebrooks.common.util.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@ComponentScan(basePackageClasses = ImageModuleConfiguration.class)
@ConfigurationProperties(prefix = "yaml")
@PropertySource(value = "classpath:application-image.yml", factory = YamlPropertySourceFactory.class)
public class ImageModuleConfiguration {

}
