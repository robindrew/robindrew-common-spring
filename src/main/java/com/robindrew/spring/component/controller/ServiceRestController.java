package com.robindrew.spring.component.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robindrew.spring.component.service.ServiceDefinition;

@RestController
@RequestMapping("/api/service/")
public class ServiceRestController {

	@Autowired
	private ServiceDefinition definition;

	@GetMapping("/v1/ping")
	public String ping() {
		return "pong";
	}

	@GetMapping("/v1/service/definition")
	public ServiceDefinition getServiceDefinition() {
		return definition;
	}

}
