package com.robindrew.spring.component.velocity;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@EnableAutoConfiguration
public class VelocityViewConfigurer implements WebMvcConfigurer {

	private final VelocityViewResolver resolver;

	public VelocityViewConfigurer(VelocityViewResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.viewResolver(resolver);
	}

}
