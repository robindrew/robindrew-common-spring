package com.robindrew.common.base.system;

import java.util.List;
import java.util.Optional;

public interface ISystemManager {

	int getLogicalCores();
	
	long getStartDate();

	long getUptime();

	long getTotalMemory();

	long getUsedMemory();

	long getFreeMemory();

	List<IDisk> getDiskList();

	List<IProcess> getProcessList();

	Optional<IProcess> getProcess(int processId);

	String getCommandLine(int processId);

	double getCpu(int processId, int seconds);

	void update();

}
