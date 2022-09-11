package com.robindrew.common.base.system;

import oshi.software.os.OSProcess;

public class OshiProcess implements IProcess {

	private final OSProcess process;

	public OshiProcess(OSProcess process) {
		this.process = process;
	}

	@Override
	public int getPid() {
		return process.getProcessID();
	}

	@Override
	public String getName() {
		return process.getName();
	}

	@Override
	public long getFileCount() {
		return process.getOpenFiles();
	}

	@Override
	public int getThreadCount() {
		return process.getThreadCount();
	}

	@Override
	public String toString() {
		return "pid=" + process.getProcessID() + ", name=" + process.getName() + ", files=" + process.getOpenFiles() + ", thread=" + process.getThreadCount();

	}

}
