package com.robindrew.common.base;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.TimeUnit;

public class TestProcess {

	public static void main(String[] args) {
		while (true) {
			System.out.println(Java.usedMemory());
			Threads.sleep(1, SECONDS);
		}
	}
}
