package com.robindrew.common.base.system;

public interface IProcess {

	int getPid();

	String getName();

	long getFileCount();

	int getThreadCount();

}
