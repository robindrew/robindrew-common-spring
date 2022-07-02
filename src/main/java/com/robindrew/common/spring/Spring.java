package com.robindrew.common.spring;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.robindrew.common.spring.service.ServiceDefinition;

public class Spring {

	private static final Logger log = LoggerFactory.getLogger(Spring.class);

	private static volatile ApplicationContext context;

	public static ApplicationContext getContext() {
		if (context == null) {
			throw new NullPointerException("context not set");
		}
		return context;
	}

	public static void setContext(ApplicationContext context) {
		if (context == null) {
			throw new NullPointerException("context");
		}
		Spring.context = context;
	}

	public static void logBeans(ApplicationContext context) {
		if (log.isDebugEnabled()) {
			Set<BeanName> names = new TreeSet<>();
			for (String name : context.getBeanDefinitionNames()) {
				Object bean = context.getBean(name);
				names.add(new BeanName(name, bean));
			}
			for (BeanName name : names) {
				if (name.isTyped()) {
					log.debug("[Typed Bean] {}", name.getName());
				} else {
					log.debug("[Named Bean] {} ({})", name.getName(), name.getType());
				}
			}
		}

		log.info("[Application Context] {} beans registered", context.getBeanDefinitionCount());
	}

	public static void logEndpoints(RequestMappingHandlerMapping mapping) {
		Map<RequestMappingInfo, HandlerMethod> methodMap = mapping.getHandlerMethods();
		Map<String, String> logMap = new TreeMap<>();
		for (Entry<RequestMappingInfo, HandlerMethod> entry : methodMap.entrySet()) {
			logMap.put(entry.getKey().toString(), entry.getValue().toString());
		}
		logMap.forEach((key, value) -> log.info("[REST Endpoint] {} -> {}", key, value));
	}

	public static void logService(ServiceDefinition service) {
		log.info("[{}] Profiles: {}", service.getName(), service.getProfiles());
		log.info("[{}] Env:      {}", service.getName(), service.getEnv());
		log.info("[{}] Port:     {}", service.getName(), service.getPort());
		log.info("[{}] Instance: {}", service.getName(), service.getInstance());
	}

	public static void logServlets(ServletContext context) {
		Map<String, ? extends ServletRegistration> servletMap = context.getServletRegistrations();
		for (ServletRegistration servlet : servletMap.values()) {
			log.info("[HTTP Servlet] {} -> {}", servlet.getMappings(), servlet.getName());
		}
	}

	static class BeanName implements Comparable<BeanName> {

		private final String name;
		private final String type;
		private final boolean typed;

		public BeanName(String name, Object bean) {
			this.name = name;
			this.type = bean.getClass().getName();
			this.typed = name.equals(type);
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
