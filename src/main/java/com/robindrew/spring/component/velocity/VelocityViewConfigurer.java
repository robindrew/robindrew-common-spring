package com.robindrew.spring.component.velocity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@EnableAutoConfiguration
public class VelocityViewConfigurer implements WebMvcConfigurer {

	@Autowired
	private VelocityViewResolver resolver;
	
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.viewResolver(resolver);
	}

}
