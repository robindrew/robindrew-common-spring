package com.robindrew.spring.component.status;

import static java.lang.System.currentTimeMillis;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robindrew.common.base.SafeRunnable;
import com.robindrew.common.base.Threads;

public abstract class AbstractStatusMonitor extends SafeRunnable implements IStatusMonitor {

	private static final Logger log = LoggerFactory.getLogger(AbstractStatusMonitor.class);

	private volatile Threshold<?> threshold;

	protected AbstractStatusMonitor(long period, TimeUnit unit) {
		withInterval(period, unit);
	}

	public AbstractStatusMonitor withInterval(long period, TimeUnit unit) {
		this.threshold = new Threshold<>(period, unit);
		return this;
	}

	@Override
	public Threshold<?> getThreshold() {
		return threshold;
	}

	@Override
	public void loggedRun() {

		log.info("[Started] {} ({} {} interval)", getClass().getSimpleName(), threshold.getPeriod(), threshold.getUnit());

		while (true) {
			long nextInterval = threshold.toMillis();
			long updateInterval = update();
			if (nextInterval > updateInterval && updateInterval > 0) {
				nextInterval = updateInterval;
			}
			sleepUntil(nextInterval);
		}
	}

	private void sleepUntil(long interval) {
		long now = currentTimeMillis();
		long next = ((now / interval) * interval) + interval;
		long sleep = next - currentTimeMillis();
		if (sleep > 0) {
			Threads.sleep(sleep);
		}
	}
}
