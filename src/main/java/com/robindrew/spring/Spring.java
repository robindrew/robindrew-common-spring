package com.robindrew.spring;

import java.util.Arrays;
import java.util.LinkedHashSet;
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

import com.robindrew.common.base.Java;
import com.robindrew.common.date.Dates;
import com.robindrew.common.text.Strings;
import com.robindrew.spring.component.service.ServiceDefinition;

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

	public static <T> T getBean(Class<T> type) {
		return getContext().getBean(type);
	}

	public static void logBeans(ApplicationContext context, String... packageNames) {
		Set<String> set = new LinkedHashSet<>(Arrays.asList(packageNames));
		logBeans(context, set);
	}

	public static void logBeans(ApplicationContext context, Set<String> packageNames) {
		for (String packageName : packageNames) {
			logBeans(context, packageName);
		}
	}

	public static void logBeans(ApplicationContext context, String packageName) {
		log.info("[Beans] Package: '{}'", packageName);

		for (BeanName name : getBeanNames(context, packageName)) {
			if (name.isTyped()) {
				log.info("[Typed Bean] {}", format(name.getName()));
			} else {
				log.info("[Named Bean] {} ({})", format(name.getName()), format(name.getType()));
			}
		}
	}

	private static String format(String name) {
		int index = name.indexOf("$$EnhancerBySpring");
		if (index == -1) {
			return name;
		}
		return name.substring(0, index) + " <<Enhanced>>";
	}

	public static void logBeans(ApplicationContext context) {
		if (log.isDebugEnabled()) {
			for (BeanName name : getBeanNames(context, null)) {
				if (name.isTyped()) {
					log.debug("[Typed Bean] {}", format(name.getName()));
				} else {
					log.debug("[Named Bean] {} ({})", format(name.getName()), format(name.getType()));
				}
			}
		}

		log.info("[Application Context] {} beans registered", context.getBeanDefinitionCount());
	}

	private static Set<BeanName> getBeanNames(ApplicationContext context, String packageName) {
		Set<BeanName> names = new TreeSet<>();
		for (String name : context.getBeanDefinitionNames()) {
			Object bean = context.getBean(name);
			if (packageName == null || bean.getClass().getPackage().getName().startsWith(packageName)) {
				names.add(new BeanName(name, bean));
			}
		}
		return names;
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
		log.info("[{}] Started:  {}", service.getName(), getStartTime());
		log.info("[{}] Process:  {}", service.getName(), Java.getProcessId());
		log.info("[{}] Memory:   {}", service.getName(), getMemory());
		log.info("[{}] Profiles: {}", service.getName(), service.getProfiles());
		log.info("[{}] Env:      {}", service.getName(), service.getEnv());
		log.info("[{}] Port:     {}", service.getName(), service.getPort());
		log.info("[{}] Instance: {}", service.getName(), service.getInstance());
	}

	private static String getMemory() {
		long max = Java.maxMemory();
		return Strings.bytes(max);
	}

	private static String getStartTime() {
		long time = Java.getStartTime();
		return Dates.formatDate("yyyy-MM-dd HH:mm:ss,S", time);
	}

	public static void logServlets(ServletContext context) {
		Map<String, ? extends ServletRegistration> servletMap = context.getServletRegistrations();
		Map<String, String> logMap = new TreeMap<>();
		for (ServletRegistration servlet : servletMap.values()) {
			logMap.put(servlet.getMappings().toString(), servlet.getName());
		}
		logMap.forEach((key, value) -> log.info("[HTTP Servlet] {} -> {}", key, value));
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
