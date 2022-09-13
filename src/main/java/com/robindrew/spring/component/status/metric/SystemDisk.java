package com.robindrew.spring.component.status.metric;

import com.robindrew.spring.component.status.Threshold;
import com.robindrew.spring.component.status.ThresholdSet;

import oshi.software.os.OSFileStore;

public class SystemDisk extends AbstractPercentMetric {

	private final OSFileStore store;
	private final ThresholdSet<Double> thresholds;
	private final String mount;

	public SystemDisk(OSFileStore store, ThresholdSet<Double> thresholds) {
		this.store = store;
		this.thresholds = thresholds;

		String mount = store.getMount();
		int index = mount.lastIndexOf(":\\");
		if (index != -1) {
			mount = "Drive(" + mount.substring(0, index) + ")";
		}
		this.mount = mount;
	}

	@Override
	public void update() {
		set(store.getTotalSpace(), store.getFreeSpace());
	}

	@Override
	public StringBuilder appendTo(StringBuilder line) {
		return appendTo(line, mount);
	}

	@Override
	public Threshold<?> apply(Threshold<?> threshold) {
		return thresholds.merge(getPercent(), threshold);
	}

}
