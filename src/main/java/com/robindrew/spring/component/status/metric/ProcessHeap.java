package com.robindrew.spring.component.status.metric;

import static com.robindrew.common.base.Java.freeMemory;
import static com.robindrew.common.base.Java.maxMemory;

import com.robindrew.spring.component.status.Threshold;
import com.robindrew.spring.component.status.ThresholdSet;

public class ProcessHeap extends AbstractPercentMetric {

	private final ThresholdSet<Double> thresholds;

	public ProcessHeap(ThresholdSet<Double> thresholds) {
		this.thresholds = thresholds;
	}

	@Override
	public void update() {
		set(maxMemory(), freeMemory());
	}

	public StringBuilder appendTo(StringBuilder line) {
		return appendTo(line, "Heap");
	}

	@Override
	public Threshold<?> apply(Threshold<?> threshold) {
		return thresholds.merge(getPercent(), threshold);
	}

}
