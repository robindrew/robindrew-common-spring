package com.robindrew.common.base;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.TreeMap;

import com.robindrew.common.text.Strings;

import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

public class Process {

	public static void main(String[] args) throws IOException {
		System.out.println(ManagementFactory.getRuntimeMXBean().getName());
		SystemInfo info = new SystemInfo();
		OperatingSystem os = info.getOperatingSystem();
		System.out.println(os.getFamily());
		Map<Integer, OSProcess> map = new TreeMap<>();
		for (OSProcess process : os.getProcesses()) {
			map.put(process.getProcessID(), process);
		}
		for (OSProcess process : map.values()) {
			if (process.getName().equals("java") || process.getName().equals("javaw")) {
				String cmd = process.getCommandLine();
				if (cmd.contains("TestProcess")) {
					System.out.println(process.getProcessID() + ". " + process.getCommandLine());
					java.lang.Process p = Runtime.getRuntime().exec(new String[] { "taskkill", "/F", "/pid", String.valueOf(process.getProcessID()) });
					print(p.getInputStream());
					print(p.getErrorStream());
				}
			}
		}
		for (OSFileStore store : os.getFileSystem().getFileStores()) {
			System.out.println(Strings.toString(store));
		}

		HardwareAbstractionLayer hardware = info.getHardware();
		System.out.println("Available: " + hardware.getMemory().getAvailable());
		System.out.println("Total: " + hardware.getMemory().getTotal());
		System.out.println("Percent: " + Strings.percent(hardware.getMemory().getAvailable(), hardware.getMemory().getTotal()));
		System.out.println(hardware.getComputerSystem().getSerialNumber());
		for (HWDiskStore disk : hardware.getDiskStores()) {
			for (HWPartition partition : disk.getPartitions()) {
				System.out.println(Strings.toString(partition));
			}
		}
	}

	private static void print(InputStream stream) throws IOException {
		while (true) {
			int r = stream.read();
			if (r == -1) {
				break;
			}
			System.out.print((char) r);
		}
	}

}
