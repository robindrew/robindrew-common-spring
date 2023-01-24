package com.robindrew.spring.component.status.metric;

import static com.robindrew.common.text.Strings.bytes;
import static java.lang.Long.MIN_VALUE;

import com.robindrew.common.text.Strings;

public abstract class AbstractPercentMetric implements IMetric {

	private volatile long free = MIN_VALUE;
	private volatile long total = MIN_VALUE;

	protected void set(long total, long free) {
		this.total = total;
		this.free = free;
	}

	public long getUsed() {
		return total - free;
	}

	public long getTotal() {
		return total;
	}

	public long getFree() {
		return free;
	}

	public double getPercent() {
		return (getUsed() / getTotal()) * 100.0;
	}

	@Override
	public boolean isValid() {
		return free != MIN_VALUE && total != MIN_VALUE;
	}

	protected StringBuilder appendTo(StringBuilder line, String name) {
		line.append(name).append(": ");
		line.append(bytes(getUsed())).append(" / ").append(bytes(getTotal()));
		line.append(" (").append(Strings.percent(getUsed(), getTotal())).append(")");
		return line;
	}

}
