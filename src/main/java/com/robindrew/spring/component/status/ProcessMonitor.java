package com.robindrew.spring.component.status;

import static com.robindrew.common.text.Strings.bytes;
import static com.robindrew.common.text.Strings.number;
import static java.math.RoundingMode.HALF_UP;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.event.Level.WARN;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robindrew.common.base.Java;
import com.robindrew.common.base.Oshi;
import com.robindrew.common.text.Strings;

import oshi.software.os.OSProcess;

public class ProcessMonitor extends AbstractStatusMonitor {

	private static final Logger log = LoggerFactory.getLogger(ProcessMonitor.class);

	private volatile OSProcess previous = null;
	private volatile ThresholdSet<Double> heapThresholds = new ThresholdSet<>();
	private volatile ThresholdSet<Double> cpuThresholds = new ThresholdSet<>();

	public ProcessMonitor() {
		super(15, SECONDS);

		heapThresholds.add(new PercentThreshold(75, WARN, 10, SECONDS));
		heapThresholds.add(new PercentThreshold(85, WARN, 5, SECONDS));

		cpuThresholds.add(new PercentThreshold(75, WARN, 10, SECONDS));
		cpuThresholds.add(new PercentThreshold(85, WARN, 5, SECONDS));
	}

	@Override
	public long update() {
		Oshi oshi = new Oshi();
		OSProcess process = oshi.getProcess();
		Threshold<?> threshold = getThreshold();

		StringBuilder line = new StringBuilder("[Process]");
		threshold = monitorProcessHeap(threshold, line);
		threshold = monitorProcessCpu(threshold, oshi, process, line);
		threshold = monitorProcessInfo(threshold, oshi, process, line);
		threshold.print(log, line);

		return threshold.toMillis();
	}

	private Threshold<?> monitorProcessInfo(Threshold<?> threshold, Oshi oshi, OSProcess process, StringBuilder line) {
		line.append(", Threads: ").append(number(process.getThreadCount()));
		line.append(", Files: ").append(number(process.getOpenFiles()));

		return threshold;
	}

	private Threshold<?> monitorProcessHeap(Threshold<?> threshold, StringBuilder line) {

		long javaUsed = Java.usedMemory();
		long javaMax = Java.maxMemory();
		double javaPercent = getPercent(javaUsed, javaMax);

		line.append(" Heap: ");
		line.append(bytes(javaUsed)).append(" / ").append(bytes(javaMax));
		line.append(" (").append(Strings.percent(javaUsed, javaMax)).append(")");

		return heapThresholds.merge(javaPercent, threshold);
	}

	private Threshold<?> monitorProcessCpu(Threshold<?> threshold, Oshi oshi, OSProcess process, StringBuilder line) {
		if (previous != null) {
			double cpu = oshi.getCpu(previous, process);
			line.append(", CPU: ").append(new BigDecimal(cpu).setScale(2, HALF_UP)).append("%");
			threshold = cpuThresholds.merge(cpu, threshold);
		}
		previous = process;

		return threshold;
	}

	private double getPercent(double dividend, double divisor) {
		return (dividend / divisor) * 100.0;
	}

}
