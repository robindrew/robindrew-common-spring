package com.robindrew.spring.logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.Set;

@Configuration
public class SpringLoggerConfig {

    @Autowired
    private ApplicationContext context;
    private Set<String> excludePackages = Set.of("io.micrometer", "org.springframework");

    @EventListener
    public void handleContextRefreshEvent(ContextRefreshedEvent event) {
        SpringLogger logger = new SpringLogger(context);
        logger.logBeans(excludePackages);
        logger.logEndpoints();
        logger.logServlets();
        logger.logPropertySources();
        logger.logService();
    }

}
