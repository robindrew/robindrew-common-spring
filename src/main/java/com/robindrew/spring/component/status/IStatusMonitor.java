package com.robindrew.spring.component.status;

public interface IStatusMonitor extends Runnable {

	long update();

	Threshold<?> getThreshold();

}
