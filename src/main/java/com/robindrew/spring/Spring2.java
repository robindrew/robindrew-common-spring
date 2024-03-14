package com.robindrew.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySources;

import java.util.*;

public class Spring2 {

    private final ApplicationContext context;

    public Spring2(ApplicationContext context) {
        this.context = context;
    }

    public <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }

    public <T> Optional<T> getOptionalBean(Class<T> type) {
        try {
            return Optional.of(getBean(type));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Map<String, Object> getBeanMap() {
        Map<String, Object> beanMap = new LinkedHashMap<>();
        for (String name : context.getBeanDefinitionNames()) {
            Object bean = context.getBean(name);
            beanMap.put(name, bean);
        }
        return beanMap;
    }

    public ConfigurableEnvironment getEnvironment() {
        return getBean(ConfigurableEnvironment.class);
    }

    public PropertySources getPropertySources() {
        return getEnvironment().getPropertySources();
    }

    public Set<String> getActiveProfiles() {
        return Set.of(getEnvironment().getActiveProfiles());
    }

    public String getServiceName(String defaultValue) {
        Map<String, Object> beanMap = context.getBeansWithAnnotation(SpringBootApplication.class);
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            if (entry.getValue() != null) {
                return getServiceName(entry);
            }
        }
        return defaultValue;
    }

    private String getServiceName(Map.Entry<String, Object> entry) {
        String name = entry.getValue().getClass().getSimpleName();
        int index = name.indexOf("$");
        if (index != -1) {
            name = name.substring(0, index);
        }
        return name;
    }


}
