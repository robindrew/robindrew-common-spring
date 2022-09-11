package com.robindrew.common.base.system;

import com.robindrew.common.text.Strings;

import oshi.software.os.OSFileStore;

public class OshiDisk implements IDisk {

	private final OSFileStore store;

	public OshiDisk(OSFileStore store) {
		this.store = store;
	}

	@Override
	public String getMount() {
		return store.getMount();
	}

	@Override
	public long getUsed() {
		return store.getTotalSpace() - store.getFreeSpace();
	}

	@Override
	public long getFree() {
		return store.getFreeSpace();
	}

	@Override
	public long getTotal() {
		return store.getTotalSpace();
	}
	
	@Override
	public String toString() {
		return Strings.toString(this);
	}

}
