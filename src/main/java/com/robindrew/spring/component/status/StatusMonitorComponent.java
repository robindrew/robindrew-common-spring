package com.robindrew.spring.component.status;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;

import org.springframework.stereotype.Component;

import com.robindrew.common.base.Threads;

@Component
public class StatusMonitorComponent {

	private final Set<IStatusMonitor> monitorSet = new CopyOnWriteArraySet<>();

	private final ExecutorService executor = Threads.newCachedThreadPool("StatusMonitor-%d");
	
	public StatusMonitorComponent() {
		register(new ProcessMonitor());
		register(new SystemMonitor("C:\\"));
	}

	public void register(IStatusMonitor monitor) {
		if (monitor == null) {
			throw new NullPointerException("monitor");
		}
		if (monitorSet.add(monitor)) {
			executor.submit(monitor);
		}
	}

}
