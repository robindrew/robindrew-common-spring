package com.robindrew.spring.component.stats;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.robindrew.common.base.Java;
import com.robindrew.common.base.SafeRunnable;

/**
 * The stats component.
 */
@Component
@ManagedResource(objectName = "service.spring.bean:type=StatsComponent")
public class StatsComponent extends SafeRunnable {

	public static final String JAVA_HEAP_MAX = "javaHeapMax";
	public static final String JAVA_HEAP_USED = "javaHeapUsed";
	public static final String JAVA_HEAP_FREE = "javaHeapFree";

	public static final String SYSTEM_MEMORY_MAX = "systemMemoryMax";
	public static final String SYSTEM_MEMORY_USED = "systemMemoryUsed";
	public static final String SYSTEM_MEMORY_FREE = "systemMemoryFree";

	@Value("${stats.component.frequency.period}")
	private long period;
	@Value("${stats.component.frequency.unit}")
	private TimeUnit unit;
	@Autowired
	private IStatsCache cache;

	@PostConstruct
	private void schedule() {
		newScheduledThreadPool(1).scheduleAtFixedRate(this, 0, period, unit);
	}

	@ManagedAttribute
	public Set<String> getKeys() {
		return cache.getKeys();
	}

	@ManagedOperation
	public Set<IStatsInstant> getStats(String key) {
		return cache.getStats(key);
	}

	public void loggedRun() {
		long timestamp = (System.currentTimeMillis() / 1000) * 1000;

		setJavaHeapStats(timestamp);
		setSystemMemoryStats(timestamp);
	}

	private void setJavaHeapStats(long timestamp) {
		long free = Java.freeMemory();
		long max = Java.maxMemory();
		long used = max - free;

		IStatsInstant javaHeapUsed = new StatsInstant(JAVA_HEAP_USED, used, timestamp);
		IStatsInstant javaHeapFree = new StatsInstant(JAVA_HEAP_FREE, free, timestamp);
		IStatsInstant javaHeapMax = new StatsInstant(JAVA_HEAP_MAX, max, timestamp);

		cache.put(javaHeapUsed);
		cache.put(javaHeapFree);
		cache.put(javaHeapMax);
	}

	private void setSystemMemoryStats(long timestamp) {
		long free = Java.getSystemFreeMemory(0);
		long max = Java.getSystemMaxMemory(1);
		long used = max - free;

		IStatsInstant systemMemoryUsed = new StatsInstant(SYSTEM_MEMORY_USED, used, timestamp);
		IStatsInstant systemMemoryFree = new StatsInstant(SYSTEM_MEMORY_FREE, free, timestamp);
		IStatsInstant systemMemoryMax = new StatsInstant(SYSTEM_MEMORY_MAX, max, timestamp);

		cache.put(systemMemoryUsed);
		cache.put(systemMemoryFree);
		cache.put(systemMemoryMax);
	}

	public IStatsCache getStatsCache() {
		return cache;
	}

}
