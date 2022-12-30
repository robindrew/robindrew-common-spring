package com.robindrew.spring.component.status;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.event.Level;

public class Threshold<V> {

	private final Level level;
	private final long period;
	private final TimeUnit unit;

	public Threshold(long period, TimeUnit unit) {
		this(Level.INFO, period, unit);
	}

	public Threshold(Level level, long period, TimeUnit unit) {
		if (level == null) {
			throw new NullPointerException("level");
		}
		if (period < 1) {
			throw new IllegalArgumentException("period=" + period);
		}
		if (unit == null) {
			throw new NullPointerException("unit");
		}
		this.level = level;
		this.period = period;
		this.unit = unit;
	}

	public Level getLevel() {
		return level;
	}

	public long getPeriod() {
		return period;
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public long toMillis() {
		return unit.toMillis(period);
	}

	public Threshold<?> merge(Threshold<?> that) {
		if (equals(that)) {
			return this;
		}
		Level level = merge(this.level, that.level);

		long period;
		TimeUnit unit;
		if (this.toMillis() < that.toMillis()) {
			period = this.getPeriod();
			unit = this.getUnit();
		} else {
			period = that.getPeriod();
			unit = that.getUnit();
		}
		return new Threshold<>(level, period, unit);
	}

	private Level merge(Level level1, Level level2) {
		return level1.ordinal() < level2.ordinal() ? level1 : level2;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Threshold) {
			Threshold<?> that = (Threshold<?>) object;
			return this.period == that.period && this.unit.equals(that.unit) && this.level.equals(that.level);
		}
		return false;
	}

	public void print(Logger log, Object message) {
		print(log, message.toString());
	}

	public void print(Logger log, String message) {
		switch (level) {
			case ERROR:
				log.error(message);
				return;
			case WARN:
				log.warn(message);
				return;
			case INFO:
				log.info(message);
				return;
			case DEBUG:
				log.debug(message);
				return;
			case TRACE:
				log.trace(message);
				return;
			default:
				throw new IllegalStateException("Unsupported level: " + level);
		}
	}

	public boolean matches(V value) {
		return false;
	}

}
