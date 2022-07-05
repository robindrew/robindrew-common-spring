package com.robindrew.spring;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.robindrew.spring.component.service.ServiceDefinition;

@ComponentScan(basePackages = "com.robindrew.spring.component")
@ServletComponentScan(basePackages = "com.robindrew.spring.servlet")
public abstract class AbstractSpringService {

	@Autowired
	private ServiceDefinition service;
	@Autowired
	private ApplicationContext context;
	@Autowired
	private ServletContext servletContext;
	@Autowired
	@Qualifier("requestMappingHandlerMapping")
	private RequestMappingHandlerMapping handlerMapping;

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
		Spring.logBeans(context, "com.robindrew.spring.component");
		Spring.logBeans(context, getClass().getPackage().getName());

		// Log the servlets registered
		Spring.logServlets(servletContext);

		// Log the rest endpoints
		Spring.logEndpoints(handlerMapping);

		// Log the service details
		Spring.logService(service);
	}
}
