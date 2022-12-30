package com.robindrew.spring.component.status.metric;

import com.robindrew.common.base.Oshi;
import com.robindrew.spring.component.status.Threshold;
import com.robindrew.spring.component.status.ThresholdSet;

import oshi.hardware.GlobalMemory;

public class SystemMemory extends AbstractPercentMetric {

	private final Oshi oshi;
	private final ThresholdSet<Double> thresholds;

	public SystemMemory(Oshi oshi, ThresholdSet<Double> thresholds) {
		this.oshi = oshi;
		this.thresholds = thresholds;
	}

	@Override
	public void update() {
		GlobalMemory memory = oshi.getMemory();
		set(memory.getTotal(), memory.getAvailable());
	}

	@Override
	public StringBuilder appendTo(StringBuilder line) {
		return appendTo(line, "Memory");
	}

	@Override
	public Threshold<?> apply(Threshold<?> threshold) {
		return thresholds.merge(getPercent(), threshold);
	}

}
