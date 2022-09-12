package com.robindrew.spring.component.status;

import static com.robindrew.common.text.Strings.bytes;
import static com.robindrew.common.text.Strings.percent;
import static java.math.RoundingMode.HALF_UP;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.event.Level.WARN;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robindrew.common.base.Oshi;

import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSFileStore;

public class SystemMonitor extends AbstractStatusMonitor {

	private static final Logger log = LoggerFactory.getLogger(SystemMonitor.class);

	private volatile long[] previousLoadTicks;
	private final Set<String> mounts = new CopyOnWriteArraySet<>();

	private volatile ThresholdSet<Double> memoryThresholds = new ThresholdSet<>();
	private volatile ThresholdSet<Double> cpuThresholds = new ThresholdSet<>();
	private volatile ThresholdSet<Double> diskThresholds = new ThresholdSet<>();

	public SystemMonitor() {
		super(1, MINUTES);

		memoryThresholds.add(new PercentThreshold(90, WARN, 30, SECONDS));
		cpuThresholds.add(new PercentThreshold(90, WARN, 30, SECONDS));
		diskThresholds.add(new PercentThreshold(90, WARN, 30, SECONDS));
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
		Oshi oshi = new Oshi();

		Threshold<?> threshold = getThreshold();

		StringBuilder line = new StringBuilder("[System]");
		threshold = monitorSystemMemory(threshold, oshi, line);
		threshold = monitorSystemCpu(threshold, oshi, line);
		threshold = monitorSystemDisks(threshold, oshi, line);
		threshold.print(log, line);

		return threshold.toMillis();
	}

	private Threshold<?> monitorSystemDisks(Threshold<?> threshold, Oshi oshi, StringBuilder line) {
		if (mounts.isEmpty()) {
			return threshold;
		}
		for (OSFileStore store : oshi.getLocalFilestores(true)) {
			if (!mounts.contains(store.getMount())) {
				continue;
			}
			if (store.getUsableSpace() == 0) {
				continue;
			}

			long diskFree = store.getFreeSpace();
			long diskTotal = store.getTotalSpace();
			long diskUsed = diskTotal - diskFree;
			double diskPercent = getPercent(diskUsed, diskTotal);
			threshold = diskThresholds.merge(diskPercent, threshold);

			String mount = store.getMount();
			int index = mount.lastIndexOf(":\\");
			if (index != -1) {
				mount = "Drive(" + mount.substring(0, index) + "):";
			}

			line.append(", ").append(mount).append(" ");
			line.append(bytes(diskUsed)).append(" / ").append(bytes(diskTotal));
			line.append(" (").append(percent(diskUsed, diskTotal)).append(")");
		}

		return threshold;
	}

	private Threshold<?> monitorSystemCpu(Threshold<?> threshold, Oshi oshi, StringBuilder line) {
		CentralProcessor processor = oshi.getProcessor();

		long[] latestLoadTicks = processor.getSystemCpuLoadTicks();
		if (previousLoadTicks != null) {
			double cpu = (processor.getSystemCpuLoadBetweenTicks(previousLoadTicks)) * 100.0;
			line.append(", CPU: ").append(new BigDecimal(cpu).setScale(2, HALF_UP)).append("%");
			threshold = cpuThresholds.merge(cpu, threshold);
		}
		previousLoadTicks = latestLoadTicks;

		return threshold;
	}

	private Threshold<?> monitorSystemMemory(Threshold<?> threshold, Oshi oshi, StringBuilder line) {
		GlobalMemory memory = oshi.getMemory();

		long memoryFree = memory.getAvailable();
		long memoryTotal = memory.getTotal();
		long memoryUsed = memoryTotal - memoryFree;
		double systemPercent = getPercent(memoryUsed, memoryTotal);

		line.append(" Memory: ");
		line.append(bytes(memoryUsed)).append(" / ").append(bytes(memoryTotal));
		line.append(" (").append(percent(memoryUsed, memoryTotal)).append(")");

		return memoryThresholds.merge(systemPercent, threshold);
	}

	private double getPercent(double dividend, double divisor) {
		return (dividend / divisor) * 100.0;
	}

}
