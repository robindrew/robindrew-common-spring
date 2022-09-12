package com.robindrew.spring.component.status;

import java.util.concurrent.TimeUnit;

import org.slf4j.event.Level;

public class PercentThreshold extends Threshold<Double> {

	private final double percent;

	public PercentThreshold(double percent, Level level, long period, TimeUnit unit) {
		super(level, period, unit);
		if (percent < 0.0 || percent > 100.0) {
			throw new IllegalArgumentException("percent=" + percent);
		}
		this.percent = percent;
	}

	public boolean matches(Double value) {
		return value >= percent;
	}

}
