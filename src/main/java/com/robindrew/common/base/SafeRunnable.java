package com.robindrew.common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

public abstract class SafeRunnable implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(SafeRunnable.class);

	private String name = null;
	private boolean logStarted = false;
	private boolean logFinished = false;
	private boolean propagateThrowable = true;

	public SafeRunnable() {
	}

	public SafeRunnable(String name) {
		this.name = name;
	}

	public String getName() {
		return name != null ? name : getClass().getName();
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPropagateThrowable() {
		return propagateThrowable;
	}

	public void setPropagateThrowable(boolean propagateThrowable) {
		this.propagateThrowable = propagateThrowable;
	}

	public boolean isLogStarted() {
		return logStarted;
	}

	public boolean isLogFinished() {
		return logFinished;
	}

	public void setLogStarted(boolean logStarted) {
		this.logStarted = logStarted;
	}

	public void setLogFinished(boolean logFinished) {
		this.logFinished = logFinished;
	}

	@Override
	public void run() {
		Stopwatch timer = Stopwatch.createStarted();
		try {
			handleStarted();
			loggedRun();
			timer.stop();
			handleFinished(timer);
		} catch (Throwable t) {
			timer.stop();
			handleCrashed(t, timer);
		}
	}

	public abstract void loggedRun();

	protected void handleFinished(Stopwatch timer) {
		if (logFinished) {
			log.info("[{}] Finished in {}", getName(), timer);
		}
	}

	protected void handleStarted() {
		if (logStarted) {
			log.info("[{}] Started", getName());
		}
	}

	private void handleCrashed(Throwable t, Stopwatch timer) {
		log.error("[" + getName() + "] Crashed after " + timer, t);
		if (isPropagateThrowable()) {
			Java.propagate(t);
		}
	}

}
