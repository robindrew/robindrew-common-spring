package com.robindrew.spring.component.status.metric;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.robindrew.spring.component.status.Threshold;

public class MetricSet {

	private final List<IMetric> metricList = new ArrayList<>();
	private final String name;

	public MetricSet(String name) {
		this.name = name;
	}

	public void add(IMetric metric) {
		if (metric == null) {
			throw new NullPointerException("metric");
		}
		metricList.add(metric);
	}

	public void addAll(IMetric... metrics) {
		for (IMetric metric : metrics) {
			add(metric);
		}
	}

	public void addAll(Collection<? extends IMetric> metrics) {
		for (IMetric metric : metrics) {
			add(metric);
		}
	}

	public Threshold<?> apply(Threshold<?> threshold) {
		for (IMetric metric : metricList) {
			if (metric.isValid()) {
				threshold = metric.apply(threshold);
			}
		}
		return threshold;
	}

	public void update() {
		for (IMetric metric : metricList) {
			metric.update();
		}
	}

	@Override
	public String toString() {
		StringBuilder line = new StringBuilder();
		line.append(name);
		boolean first = true;
		for (IMetric metric : metricList) {
			if (metric.isValid()) {
				if (!first) {
					line.append(',');
				}
				line.append(' ');
				first = false;

				metric.appendTo(line);
			}
		}
		return line.toString();
	}

}
