package com.robindrew.spring.component.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robindrew.spring.component.service.ServiceDefinition;

@RestController
@RequestMapping("/api/service/")
public class ServiceRestController {

	private final ServiceDefinition definition;
	
	public ServiceRestController(ServiceDefinition definition) {
		this.definition = definition;
	}
	
	@GetMapping("/v1/ping")
	public String ping() {
		return "pong";
	}

	@GetMapping("/v1/service/definition")
	public ServiceDefinition getServiceDefinition() {
		return definition;
	}

}
