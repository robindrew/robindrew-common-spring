package com.robindrew.spring.component.service.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.robindrew.spring.AbstractSpringService;
import com.robindrew.spring.component.service.ServiceDefinition;

public class LoggingLifecycleListener implements IServiceLifecycleListener {

	private static final Logger log = LoggerFactory.getLogger(AbstractSpringService.class);
	
	private final Stopwatch startupTimer;

	public LoggingLifecycleListener(Stopwatch startupTimer) {
		this.startupTimer = startupTimer;
	}

	@Override
	public void started(ServiceDefinition service) {
		if (startupTimer.isRunning()) {
			startupTimer.stop();
		}
		log.info("[{}] Service started in {}", service.getName(), startupTimer);
	}

}
