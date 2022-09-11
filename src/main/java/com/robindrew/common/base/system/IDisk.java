package com.robindrew.common.base.system;

public interface IDisk {

	String getMount();

	long getUsed();

	long getFree();

	long getTotal();

}
