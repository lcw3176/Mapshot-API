package com.joebrooks.notice;

import com.joebrooks.common.util.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EntityScan(basePackageClasses = NoticeModuleConfiguration.class)
@EnableJpaRepositories(basePackageClasses = NoticeModuleConfiguration.class)
@ComponentScan(basePackageClasses = NoticeModuleConfiguration.class)
@ConfigurationProperties(prefix = "yaml")
@PropertySource(value = "classpath:application-notice.yml", factory = YamlPropertySourceFactory.class)
public class NoticeModuleConfiguration {

}
