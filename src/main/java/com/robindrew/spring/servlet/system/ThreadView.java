package com.robindrew.spring.servlet.system;

import java.lang.Thread.State;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class ThreadView implements Comparable<ThreadView> {

	private final Thread thread;
	private final StackTraceElement[] elements;

	public ThreadView(Thread thread, StackTraceElement[] elements) {
		this.thread = thread;
		this.elements = elements;
	}

	public Thread getThread() {
		return thread;
	}

	public long getId() {
		return thread.getId();
	}

	public int getPriority() {
		return thread.getPriority();
	}

	public String getName() {
		return thread.getName();
	}

	public State getState() {
		return thread.getState();
	}

	public boolean isDaemon() {
		return thread.isDaemon();
	}

	public String getStackTrace() {
		if (elements.length == 0) {
			return "-";
		}
		StringBuilder trace = new StringBuilder();
		for (StackTraceElement element : elements) {
			trace.append(element).append("<br/>");
		}
		return trace.toString();
	}

	@Override
	public int compareTo(ThreadView that) {
		CompareToBuilder compare = new CompareToBuilder();
		compare.append(this.getId(), that.getId());
		compare.append(this.getName(), that.getName());
		compare.append(this.getState(), that.getState());
		return compare.toComparison();
	}

}
