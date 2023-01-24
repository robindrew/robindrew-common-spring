package com.robindrew.common.base;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;

public class OshiTest {

	@Test
	public void testOshiBasics() {
		Oshi oshi = new Oshi();

		// Startup
		Assert.assertTrue(oshi.getStartDate() < System.currentTimeMillis());
		Assert.assertTrue(oshi.getUptimeMillis() > 0);

		// Memory
		long free = oshi.getFreeMemory();
		long used = oshi.getUsedMemory();
		long total = oshi.getTotalMemory();
		Assert.assertTrue(total > free);
		Assert.assertTrue(total > used);

		List<OSFileStore> diskList = oshi.getFileStores(true);
		Assert.assertFalse(diskList.isEmpty());
		for (OSFileStore disk : diskList) {
			free = disk.getFreeSpace();
			total = disk.getTotalSpace();
			Assert.assertTrue(total > free);
		}

		int processId = Java.getProcessId();
		Assert.assertTrue(oshi.getProcess(processId).isPresent());
		OSProcess process = oshi.getProcess(processId).get();
		Assert.assertEquals(processId, process.getProcessID());
		Assert.assertTrue(process.getThreadCount() > 0);
		Assert.assertTrue(process.getOpenFiles() > 0);
	}

}
