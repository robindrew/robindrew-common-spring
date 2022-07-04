package com.robindrew.spring.component.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The service definition.
 */
@Component
public class ServiceDefinition {

	@Value("${service.name}")
	private String name;
	@Value("${service.port}")
	private int port;
	@Value("${service.instance}")
	private int instance;
	@Value("${service.env}")
	private String env;
	@Value("${service.profiles}")
	private Set<String> profiles;

	public String getName() {
		return name;
	}

	public int getPort() {
		return port;
	}

	public int getInstance() {
		return instance;
	}

	public String getEnv() {
		return env;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setInstance(int instance) {
		this.instance = instance;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public Set<String> getProfiles() {
		return profiles;
	}

	public void setProfiles(Set<String> profiles) {
		this.profiles = profiles;
	}

}
