package com.robindrew.common.base;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.List;
import java.util.Optional;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

public class Oshi {

	private final SystemInfo systemInfo = new SystemInfo();

	public SystemInfo getSystemInfo() {
		return systemInfo;
	}

	public List<OSFileStore> getLocalFilestores(boolean local) {
		return getSystemInfo().getOperatingSystem().getFileSystem().getFileStores(local);
	}

	public CentralProcessor getProcessor() {
		return getSystemInfo().getHardware().getProcessor();
	}

	public OperatingSystem getOperatingSystem() {
		return getSystemInfo().getOperatingSystem();
	}

	public GlobalMemory getMemory() {
		return getSystemInfo().getHardware().getMemory();
	}

	public long getStartDate() {
		return getOperatingSystem().getSystemBootTime();
	}

	public long getUptimeMillis() {
		return getOperatingSystem().getSystemUptime() * 1000;
	}

	public long getUptimeSeconds() {
		return getOperatingSystem().getSystemUptime();
	}

	public long getFreeMemory() {
		return getMemory().getAvailable();
	}

	public long getTotalMemory() {
		return getMemory().getTotal();
	}

	public long getUsedMemory() {
		return getTotalMemory() - getFreeMemory();
	}

	public int getLogicalCores() {
		return getSystemInfo().getHardware().getProcessor().getLogicalProcessorCount();
	}

	public int getProcessId() {
		return getOperatingSystem().getProcessId();
	}

	public Optional<OSProcess> getProcess(int processId) {
		return Optional.ofNullable(getOperatingSystem().getProcess(processId));
	}

	public OSProcess getProcess() {
		return getOperatingSystem().getProcess(getProcessId());
	}

	public double getCpuLoad(OSProcess before, OSProcess after) {
		return after.getProcessCpuLoadBetweenTicks(before) / getLogicalCores();
	}

	public double getCpu(OSProcess before, OSProcess after) {
		return getCpuLoad(before, after) * 100.0;
	}

	public double getCpu(int processId, int seconds) {
		OSProcess before = getOperatingSystem().getProcess(processId);
		if (before == null) {
			return Double.NaN;
		}
		Threads.sleep(seconds, SECONDS);
		OSProcess after = getOperatingSystem().getProcess(processId);
		if (after == null) {
			return Double.NaN;
		}

		return getCpu(before, after);
	}

	public List<OSFileStore> getFileStores(boolean localOnly) {
		return getOperatingSystem().getFileSystem().getFileStores(localOnly);
	}

}
