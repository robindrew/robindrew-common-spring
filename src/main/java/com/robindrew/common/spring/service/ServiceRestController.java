package com.robindrew.common.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceRestController {

	@Autowired
	private ServiceDefinition definition;

	@GetMapping("/rest/ping")
	public String ping() {
		return "pong";
	}

	@GetMapping("/rest/service/definition")
	public ServiceDefinition getServiceDefinition() {
		return definition;
	}

}
