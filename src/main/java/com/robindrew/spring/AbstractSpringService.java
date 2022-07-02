package com.robindrew.spring;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.robindrew.spring.service.ServiceDefinition;

@ServletComponentScan(basePackages = "com.robindrew.spring.servlet")
public abstract class AbstractSpringService {

	@Autowired
	private ServiceDefinition service;
	@Autowired
	@Qualifier("requestMappingHandlerMapping")
	private RequestMappingHandlerMapping handlerMapping;
	@Autowired
	private ServletContext servletContext;
	@Autowired
	private ApplicationContext context;

	public ServiceDefinition getService() {
		return service;
	}

	@PostConstruct
	public void registerContext() {
		Spring.setContext(context);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void applicationReady() {

		// Log the beans registered
		Spring.logBeans(context);

		// Log the servlets registered
		Spring.logServlets(servletContext);

		// Log the rest endpoints
		Spring.logEndpoints(handlerMapping);

		// Log the service details
		Spring.logService(service);
	}
}
