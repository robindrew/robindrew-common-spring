package com.robindrew.spring.component.servlet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.robindrew.common.template.ITemplateLocator;
import com.robindrew.common.template.velocity.VelocityTemplateLocatorSupplier;

@Configuration
public class ServletConfiguration {

	@Bean(name = "servletTemplateLocator")
	public ITemplateLocator getTemplateLocator() {
		// Velocity is the only implementation right now!
		return new VelocityTemplateLocatorSupplier().get();
	}

}
