package com.robindrew.common.base.system;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.robindrew.common.base.Threads;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;

public class OshiSystemManager implements ISystemManager {

	private volatile SystemInfo system = new SystemInfo();

	public long getStartDate() {
		return system.getOperatingSystem().getSystemBootTime();
	}

	public long getUptime() {
		return system.getOperatingSystem().getSystemUptime() * 1000;
	}

	public long getTotalMemory() {
		return system.getHardware().getMemory().getTotal();
	}

	public long getUsedMemory() {
		GlobalMemory memory = system.getHardware().getMemory();
		return memory.getTotal() - memory.getAvailable();
	}

	public long getFreeMemory() {
		return system.getHardware().getMemory().getAvailable();
	}

	@Override
	public void update() {
		system = new SystemInfo();
	}

	@Override
	public List<IDisk> getDiskList() {
		List<IDisk> list = new ArrayList<>();
		for (OSFileStore store : system.getOperatingSystem().getFileSystem().getFileStores()) {
			if (store.getUsableSpace() > 0) {
				list.add(new OshiDisk(store));
			}
		}
		return list;
	}

	@Override
	public List<IProcess> getProcessList() {
		List<IProcess> list = new ArrayList<>();
		for (OSProcess process : system.getOperatingSystem().getProcesses()) {
			list.add(new OshiProcess(process));
		}
		return list;
	}

	@Override
	public String getCommandLine(int processId) {
		OSProcess process = new SystemInfo().getOperatingSystem().getProcess(processId);
		if (process == null) {
			return null;
		}
		return process.getCommandLine();
	}

	@Override
	public double getCpu(int processId, int seconds) {
		int logical = getLogicalCores();
		OSProcess before = new SystemInfo().getOperatingSystem().getProcess(processId);
		if (before == null) {
			return Double.NaN;
		}
		Threads.sleep(seconds, SECONDS);
		OSProcess after = new SystemInfo().getOperatingSystem().getProcess(processId);
		if (after == null) {
			return Double.NaN;
		}
		return (after.getProcessCpuLoadBetweenTicks(before) / logical) * 100.0;
	}

	@Override
	public Optional<IProcess> getProcess(int processId) {
		OSProcess process = system.getOperatingSystem().getProcess(processId);
		return process == null ? empty() : of(new OshiProcess(process));
	}

	@Override
	public int getLogicalCores() {
		return system.getHardware().getProcessor().getLogicalProcessorCount();
	}

}
