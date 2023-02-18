package com.robindrew.common.io.file.cachemap;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class RandomAccessCacheMapTest {

	@Test
	public void createTempFileAndPopulate() throws Exception {
		Random random = new Random();

		int count = 1000;

		File file = File.createTempFile("RandomAccessCacheMap.", ".dat");

		ICacheMap<String, byte[]> cacheMap = new RandomAccessCacheMap(file);

		Map<String, byte[]> actualMap = new LinkedHashMap<>();

		for (int i = 0; i < count; i++) {
			String key = "Key[" + i + "]";

			int length = random.nextInt(9999) + 1;
			byte[] value = new byte[length];
			random.nextBytes(value);

			cacheMap.put(key, value);
			actualMap.put(key, value);
		}

		Iterator<Entry<String, byte[]>> entries = cacheMap.entrySet().iterator();
		for (Entry<String, byte[]> entry : actualMap.entrySet()) {
			Assert.assertTrue(cacheMap.containsKey(entry.getKey()));
			Assert.assertArrayEquals(entry.getValue(), cacheMap.get(entry.getKey()));

			Entry<String, byte[]> next = entries.next();
			Assert.assertEquals(entry.getKey(), next.getKey());
			Assert.assertArrayEquals(entry.getValue(), next.getValue());
		}

		cacheMap.clear();
		file.delete();
	}
}
