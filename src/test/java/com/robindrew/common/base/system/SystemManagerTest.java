package com.robindrew.common.base.system;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.robindrew.common.base.Java;

public class SystemManagerTest {

	@Test
	public void testOshiSystemManager() {

		ISystemManager manager = new OshiSystemManager();

		// Startup
		Assert.assertTrue(manager.getStartDate() < System.currentTimeMillis());
		Assert.assertTrue(manager.getUptime() > 0);

		// Memory
		long free = manager.getFreeMemory();
		long used = manager.getUsedMemory();
		long total = manager.getTotalMemory();
		Assert.assertTrue(total > free);
		Assert.assertTrue(total > used);

		List<IDisk> diskList = manager.getDiskList();
		Assert.assertFalse(diskList.isEmpty());
		for (IDisk disk : diskList) {
			free = disk.getFree();
			used = disk.getUsed();
			total = disk.getTotal();
			Assert.assertTrue(total > free);
			Assert.assertTrue(total > used);
		}

		int processId = Java.getProcessId();
		Assert.assertTrue(manager.getProcess(processId).isPresent());
		IProcess process = manager.getProcess(processId).get();
		Assert.assertEquals(processId, process.getPid());
		Assert.assertTrue(process.getThreadCount() > 0);
		Assert.assertTrue(process.getFileCount() > 0);
	}

}
