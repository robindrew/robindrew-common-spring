package com.robindrew.spring.component.status.metric;

import static com.robindrew.common.text.Strings.number;

import com.robindrew.common.base.Oshi;

import oshi.software.os.OSProcess;

public class ProcessThreads implements IMetric {

	private final OSProcess process;

	public ProcessThreads(Oshi oshi) {
		this.process = oshi.getProcess();
	}

	@Override
	public StringBuilder appendTo(StringBuilder line) {
		return line.append("Threads: ").append(number(process.getThreadCount()));
	}

}
