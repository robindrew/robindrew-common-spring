package com.robindrew.spring.logger;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.robindrew.common.base.Java;
import com.robindrew.common.date.Dates;
import com.robindrew.common.text.Strings;
import com.robindrew.spring.Spring2;

public class SpringLogger {

    private static final Logger log = LoggerFactory.getLogger(SpringLogger.class);

    private final Spring2 spring;
    private final String serviceName;

    public SpringLogger(ApplicationContext context) {
        this.spring = new Spring2(context);
        this.serviceName = spring.getServiceName("UnknownService");
    }

    public void logBeans(Set<String> excludedPackages) {
        for (BeanName name : getBeanNames(excludedPackages)) {
            logBean(name);
        }
    }

    private void logBean(BeanName name) {
        if (name.isTyped()) {
            log.info("[Typed Bean] {}", format(name.getName()));
        } else {
            log.info("[Named Bean] '{}' ({})", format(name.getName()), format(name.getType()));
        }
    }

    private String format(String name) {
        int index = name.indexOf("$$SpringCGLIB");
        if (index != -1) {
            return name.substring(0, index) + " <<Enhanced>>";
        }
        index = name.indexOf("$$EnhancerBySpring");
        if (index != -1) {
            return name.substring(0, index) + " <<Enhanced>>";
        }
        return name;
    }

    public void logBeans() {
        logBeans(Set.of());
    }

    public Set<BeanName> getBeanNames(Set<String> excludedPackages) {
        Set<BeanName> names = new TreeSet<>();
        for (Entry<String, Object> entry : spring.getBeanMap().entrySet()) {
            String name = entry.getKey();
            Object bean = entry.getValue();
            if (!isExcluded(bean, excludedPackages)) {
                names.add(new BeanName(name, bean));
            }
        }
        return names;
    }

    private boolean isExcluded(Object bean, Set<String> packageNames) {
        if (packageNames.isEmpty()) {
            return true;
        }
        for (String packageName : packageNames) {
            if (bean.getClass().getPackage().getName().startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }

    public void logPropertySources() {
        PropertySources sources = spring.getPropertySources();
        sources.forEach(this::logPropertySource);
    }

    private void logPropertySource(PropertySource<?> source) {

        // Config File?
        if (source instanceof OriginTrackedMapPropertySource) {
        	OriginTrackedMapPropertySource trackedSource = (OriginTrackedMapPropertySource) source;
            log.info("[Property Source] {} ({} properties)", getSourceName(source), trackedSource.getSource().size());
            return;
        }

        // Map of Properties
        if (source.getSource() instanceof Map<?, ?>) {
        	Map<?, ?> map = (Map<?, ?>) source;
            log.debug("[Property Source] {} ({} properties)", source.getName(), map.size());
            return;
        }

        // Unknown
        log.debug("[Property Source] {}", source.getName());
    }

    private String getSourceName(PropertySource<?> source) {
        String name = source.getName();
        if (name.contains(".properties")) {
            int start = name.indexOf('[');
            int end = name.indexOf(']');
            if (start != -1 && end > start) {
                return "File: '" + name.substring(start + 1, end) + "'";
            }
        }
        return name;
    }

    public void logServlets() {
        Optional<ServletContext> mapping = spring.getOptionalBean(ServletContext.class);
        mapping.ifPresent(this::logServlets);
    }

    public void logServlets(ServletContext context) {
        Map<String, ? extends ServletRegistration> servletMap = context.getServletRegistrations();
        Map<String, String> logMap = new TreeMap<>();
        for (ServletRegistration servlet : servletMap.values()) {
            log.info("Servlet {}, {}", servlet.getClassName(), servlet.getName());
            logMap.put(servlet.getMappings().toString(), servlet.getName());
        }
        logMap.forEach((key, value) -> log.info("[HTTP Servlet] {} -> {}", key, value));
    }

    public void logEndpoints() {
        Optional<RequestMappingHandlerMapping> mapping = spring.getOptionalBean(RequestMappingHandlerMapping.class);
        mapping.ifPresent(this::logEndpoints);
    }

    public void logEndpoints(RequestMappingHandlerMapping mapping) {
        Map<RequestMappingInfo, HandlerMethod> methodMap = mapping.getHandlerMethods();
        Map<String, String> logMap = new TreeMap<>();
        for (Entry<RequestMappingInfo, HandlerMethod> entry : methodMap.entrySet()) {
            logMap.put(entry.getKey().toString(), entry.getValue().toString());
        }
        logMap.forEach((key, value) -> log.info("[REST Endpoint] {} -> {}", key, value));
    }

    public void logService() {
        ConfigurableEnvironment env = spring.getEnvironment();

        log.info("[{}] Started:  {}", serviceName, getStartTime());
        log.info("[{}] Startup:  {}", serviceName, Strings.duration(Java.getUptime()));
        log.info("[{}] Process:  {}", serviceName, Java.getProcessId());
        log.info("[{}] Memory:   {}", serviceName, getMemory());

        // Profiles
        List<String> profiles = List.of(env.getActiveProfiles());
        if (profiles.isEmpty()) {
            throw new IllegalStateException("No profiles configured (System property: 'spring.profiles.active' or Environment property: 'SPRING_PROFILES_ACTIVE'");
            //log.warn("[{}] Profiles: !! NONE CONFIGURED !!", serviceName);
        } else {
            log.info("[{}] Profiles: {}", serviceName, profiles);
        }

        // Web Server
        Integer port = getWebServerPort();
        if (port != null) {
            log.info("[{}] Port:     {}", serviceName, port);
        }
    }

    public Integer getWebServerPort() {

        // Use reflection to see if there is a WebServer port
        // ServletWebServerApplicationContext.getWebServer().getPort();
        // Avoids explicit spring boot web dependencies
        try {
            Object webServer = spring.getClass().getMethod("getWebServer").invoke(spring);
            return (Integer) webServer.getClass().getMethod("getPort").invoke(webServer);
        } catch (Exception e) {
        }

        // Check the properties for a port
        String port = spring.getEnvironment().getProperty("server.port");
        if (port != null) {
            return Integer.parseInt(port);
        }
        return null;
    }

    private String getMemory() {
        long max = Java.maxMemory();
        return Strings.bytes(max);
    }

    private String getStartTime() {
        long time = Java.getStartTime();
        return Dates.formatDate("yyyy-MM-dd HH:mm:ss,S", time);
    }

    public static class BeanName implements Comparable<BeanName> {

        private final String name;
        private final String type;
        private final boolean typed;

        public BeanName(String name, Object bean) {
            this.name = name;
            this.type = bean.getClass().getName();
            this.typed = type.startsWith(name);
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public boolean isTyped() {
            return typed;
        }

        @Override
        public int compareTo(BeanName that) {
            if (this.typed != that.typed) {
                return Boolean.compare(this.typed, that.typed);
            }
            return this.name.compareTo(that.name);
        }

    }

}
