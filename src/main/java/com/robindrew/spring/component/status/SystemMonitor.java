package com.robindrew.spring.component.status;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.event.Level.WARN;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robindrew.common.base.Oshi;
import com.robindrew.spring.component.status.metric.MetricSet;
import com.robindrew.spring.component.status.metric.SystemCpu;
import com.robindrew.spring.component.status.metric.SystemDisk;
import com.robindrew.spring.component.status.metric.SystemMemory;

import oshi.software.os.OSFileStore;

public class SystemMonitor extends AbstractStatusMonitor {

	private static final Logger log = LoggerFactory.getLogger(SystemMonitor.class);

	private final Set<String> mounts = new CopyOnWriteArraySet<>();

	private final MetricSet set;
	private volatile ThresholdSet<Double> memoryThresholds = new ThresholdSet<>();
	private volatile ThresholdSet<Double> cpuThresholds = new ThresholdSet<>();
	private volatile ThresholdSet<Double> diskThresholds = new ThresholdSet<>();

	public SystemMonitor() {
		super(1, MINUTES);

		memoryThresholds.add(new PercentThreshold(90, WARN, 30, SECONDS));
		cpuThresholds.add(new PercentThreshold(90, WARN, 30, SECONDS));
		diskThresholds.add(new PercentThreshold(90, WARN, 30, SECONDS));

		Oshi oshi = new Oshi();

		// Build Metrics
		set = new MetricSet("[System]");
		set.add(new SystemMemory(oshi, memoryThresholds));
		set.add(new SystemCpu(oshi, cpuThresholds));
		set.addAll(getSystemDisks(oshi, diskThresholds));
	}

	private List<SystemDisk> getSystemDisks(Oshi oshi, ThresholdSet<Double> thresholds) {
		List<SystemDisk> list = new ArrayList<>();
		for (OSFileStore store : oshi.getFileStores(true)) {
			if (!mounts.contains(store.getMount())) {
				continue;
			}
			if (store.getUsableSpace() == 0) {
				continue;
			}
			list.add(new SystemDisk(store, thresholds));
		}
		return list;
	}

	public SystemMonitor withMount(String mount) {
		if (!mount.endsWith(":\\")) {
			throw new IllegalArgumentException("Invalid mount: '" + mount + "'");
		}
		this.mounts.add(mount);
		return this;
	}

	@Override
	public long update() {

		// Update the metrics
		set.update();

		// Apply the thresholds and log
		Threshold<?> threshold = set.apply(getThreshold());
		threshold.print(log, set);
		return threshold.toMillis();
	}

}
