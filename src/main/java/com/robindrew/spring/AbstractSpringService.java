package com.robindrew.spring;

import java.lang.management.ManagementFactory;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.event.EventListener;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.google.common.base.Stopwatch;
import com.robindrew.spring.component.service.ServiceDefinition;
import com.robindrew.spring.component.service.lifecycle.LoggingLifecycleListener;
import com.robindrew.spring.component.service.lifecycle.ServiceLifecycle;

@EnableMBeanExport
@ComponentScan(basePackages = "com.robindrew.spring.component")
@ServletComponentScan(basePackages = "com.robindrew.spring.servlet")
public abstract class AbstractSpringService {

	public static void run(Class<?> type, String[] args) {
		Stopwatch timer = Stopwatch.createStarted();
		
		// Sprint Application
		ConfigurableApplicationContext context = SpringApplication.run(type, args);
		
		ServiceLifecycle lifecycle = context.getBean(ServiceLifecycle.class);
		lifecycle.register(new LoggingLifecycleListener(timer));
		lifecycle.started();
	}

	@Autowired
	private ServiceDefinition service;
	@Autowired
	private ApplicationContext context;
	@Autowired
	private ServletContext servletContext;
	@Autowired
	@Qualifier("requestMappingHandlerMapping")
	private RequestMappingHandlerMapping handlerMapping;

	@Bean
	public MBeanServer getMBeanServer() {
		return ManagementFactory.getPlatformMBeanServer();
	}

	@Bean
	public MBeanExporter getMBeanExporter() {
		MBeanExporter exporter = new MBeanExporter();
		exporter.setServer(getMBeanServer());
		exporter.setAutodetect(true);
		exporter.setAutodetectMode(MBeanExporter.AUTODETECT_ALL);
		return exporter;
	}

	public ServiceDefinition getService() {
		return service;
	}

	public ApplicationContext getContext() {
		return context;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public RequestMappingHandlerMapping getHandlerMapping() {
		return handlerMapping;
	}

	@PostConstruct
	public void registerContext() {
		Spring.setContext(context);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void applicationReady() {

		// Log the beans registered
		Spring.logBeans(context, "com.robindrew.spring.component", getClass().getPackage().getName() + ".component");

		// Log the servlets registered
		Spring.logServlets(servletContext);

		// Log the rest endpoints
		Spring.logEndpoints(handlerMapping);

		// Log the service details
		Spring.logService(service);
	}
}
