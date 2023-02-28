package com.joebrooks.notice;

import com.joebrooks.common.util.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@ConfigurationProperties(prefix = "yaml")
@PropertySource(value = "classpath:application-notice.yml", factory = YamlPropertySourceFactory.class)
public class NoticeModuleConfiguration {

}
