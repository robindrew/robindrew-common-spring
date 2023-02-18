package com.robindrew.common.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class BytesTest {

	@Test
	public void testReadWriteBytes() {
		Random random = new Random();

		int length = random.nextInt(255) + 1;
		int count = 100;
		byte[] bytes = new byte[(length + 4) * count];
		List<byte[]> list = new ArrayList<>();

		int offset = 0;
		for (int i = 0; i < count; i++) {
			byte[] array = new byte[length];
			random.nextBytes(array);
			list.add(array);
			Bytes.writeBytes(bytes, offset, array);
			offset = offset + 4 + length;
		}

		offset = 0;
		for (int i = 0; i < count; i++) {
			byte[] expected = list.get(i);
			byte[] actual = Bytes.readBytes(bytes, offset);
			Assert.assertArrayEquals(expected, actual);
			offset = offset + 4 + length;
		}
	}

	@Test
	public void testReadWriteLong() {
		Random random = new Random();

		int length = 8;
		int count = 1000;
		byte[] bytes = new byte[length * count];
		List<Long> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			long value = random.nextLong();
			list.add(value);
			Bytes.writeLong(bytes, i * length, value);
		}

		for (int i = 0; i < count; i++) {
			long expected = list.get(i);
			long actual = Bytes.readLong(bytes, i * length);
			Assert.assertEquals(expected, actual);
		}
	}

	@Test
	public void testReadWriteInt() {
		Random random = new Random();

		int length = 4;
		int count = 1000;
		byte[] bytes = new byte[length * count];
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			int value = random.nextInt();
			list.add(value);
			Bytes.writeInt(bytes, i * length, value);
		}

		for (int i = 0; i < count; i++) {
			int expected = list.get(i);

			int actual = Bytes.readInt(bytes, i * length);
			Assert.assertEquals(expected, actual);
		}
	}

	@Test
	public void testReadWriteShort() {
		Random random = new Random();

		int length = 2;
		int count = 1000;
		byte[] bytes = new byte[length * count];
		List<Short> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			short value = (short) random.nextInt();
			list.add(value);
			Bytes.writeShort(bytes, i * length, value);
		}

		for (int i = 0; i < count; i++) {
			short expected = list.get(i);

			short actual = Bytes.readShort(bytes, i * length);
			Assert.assertEquals(expected, actual);
		}
	}

	@Test
	public void testReadWriteByte() {
		Random random = new Random();

		int length = 1;
		int count = 1000;
		byte[] bytes = new byte[length * count];
		List<Byte> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			byte value = (byte) random.nextInt();
			list.add(value);
			Bytes.writeByte(bytes, i * length, value);
		}

		for (int i = 0; i < count; i++) {
			byte expected = list.get(i);

			byte actual = Bytes.readByte(bytes, i * length);
			Assert.assertEquals(expected, actual);
		}
	}

}
