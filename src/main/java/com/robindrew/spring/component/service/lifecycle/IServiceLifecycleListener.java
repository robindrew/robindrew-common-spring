package com.robindrew.spring.component.service.lifecycle;

import com.robindrew.spring.component.service.ServiceDefinition;

public interface IServiceLifecycleListener {

	void started(ServiceDefinition service);
	
}
