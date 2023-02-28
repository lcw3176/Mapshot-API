package com.joebrooks.executor;

import com.joebrooks.image.ImageModuleConfiguration;
import com.joebrooks.notice.NoticeModuleConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackageClasses = NoticeModuleConfiguration.class)
@EnableJpaRepositories(basePackageClasses = NoticeModuleConfiguration.class)
@ComponentScan(basePackageClasses = NoticeModuleConfiguration.class)
@ComponentScan(basePackageClasses = ImageModuleConfiguration.class)
public class ContextLoader {

}
