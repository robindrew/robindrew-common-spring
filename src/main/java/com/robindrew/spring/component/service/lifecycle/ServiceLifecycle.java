package com.robindrew.spring.component.service.lifecycle;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import com.robindrew.spring.component.service.ServiceDefinition;

@Component
public class ServiceLifecycle implements IServiceLifecycleListener {

	private final ServiceDefinition service;
	private final List<IServiceLifecycleListener> listeners = new CopyOnWriteArrayList<>();

	public ServiceLifecycle(ServiceDefinition service) {
		if (service == null) {
			throw new NullPointerException("service");
		}
		this.service = service;
	}

	public void started() {
		started(service);
	}

	@Override
	public void started(ServiceDefinition service) {
		for (IServiceLifecycleListener listener : listeners) {
			listener.started(service);
		}
	}

	public void register(IServiceLifecycleListener listener) {
		if (listener == null) {
			throw new NullPointerException("listener");
		}
		listeners.add(listener);
	}

}
