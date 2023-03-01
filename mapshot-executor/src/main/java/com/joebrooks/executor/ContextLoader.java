package com.joebrooks.executor;

import com.joebrooks.image.ImageModuleConfiguration;
import com.joebrooks.notice.NoticeModuleConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ImageModuleConfiguration.class, NoticeModuleConfiguration.class})
public class ContextLoader {

}
