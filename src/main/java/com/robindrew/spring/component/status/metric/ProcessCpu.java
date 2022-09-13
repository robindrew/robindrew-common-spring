package com.robindrew.spring.component.status.metric;

import static java.lang.Double.NaN;
import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;

import com.robindrew.common.base.Oshi;
import com.robindrew.spring.component.status.Threshold;
import com.robindrew.spring.component.status.ThresholdSet;

import oshi.software.os.OSProcess;

public class ProcessCpu implements IMetric {

	private final Oshi oshi;
	private final ThresholdSet<Double> thresholds;

	private volatile double percent = NaN;
	private volatile OSProcess previous;

	public ProcessCpu(Oshi oshi, ThresholdSet<Double> thresholds) {
		this.oshi = oshi;
		this.thresholds = thresholds;
	}

	@Override
	public boolean isValid() {
		return !Double.isNaN(percent);
	}

	@Override
	public Threshold<?> apply(Threshold<?> threshold) {
		return thresholds.merge(percent, threshold);
	}

	public void update() {
		OSProcess latest = oshi.getProcess();
		if (previous != null) {
			percent = oshi.getCpu(previous, latest);
		}
		previous = latest;
	}

	public double getPercent() {
		return percent;
	}

	@Override
	public StringBuilder appendTo(StringBuilder line) {
		return line.append("CPU: ").append(new BigDecimal(percent).setScale(2, HALF_UP)).append("%");
	}

}
