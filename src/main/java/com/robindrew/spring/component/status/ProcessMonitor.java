package com.robindrew.spring.component.status;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.event.Level.WARN;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robindrew.common.base.Oshi;
import com.robindrew.spring.component.status.metric.MetricSet;
import com.robindrew.spring.component.status.metric.ProcessCpu;
import com.robindrew.spring.component.status.metric.ProcessFiles;
import com.robindrew.spring.component.status.metric.ProcessHeap;
import com.robindrew.spring.component.status.metric.ProcessThreads;

public class ProcessMonitor extends AbstractStatusMonitor {

	private static final Logger log = LoggerFactory.getLogger(ProcessMonitor.class);

	private final MetricSet set;
	private volatile ThresholdSet<Double> heapThresholds = new ThresholdSet<>();
	private volatile ThresholdSet<Double> cpuThresholds = new ThresholdSet<>();

	public ProcessMonitor() {
		super(40, SECONDS);

		// Heap monitoring thresholds
		heapThresholds.add(new PercentThreshold(60, WARN, 20, SECONDS));
		heapThresholds.add(new PercentThreshold(75, WARN, 10, SECONDS));
		heapThresholds.add(new PercentThreshold(85, WARN, 5, SECONDS));

		// CPU monitoring thresholds
		cpuThresholds.add(new PercentThreshold(60, WARN, 20, SECONDS));
		cpuThresholds.add(new PercentThreshold(75, WARN, 10, SECONDS));
		cpuThresholds.add(new PercentThreshold(85, WARN, 5, SECONDS));

		Oshi oshi = new Oshi();

		// Build Metrics
		set = new MetricSet("[Process]");
		set.add(new ProcessHeap(heapThresholds));
		set.add(new ProcessFiles(oshi));
		set.add(new ProcessThreads(oshi));
		set.add(new ProcessCpu(oshi, cpuThresholds));
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
