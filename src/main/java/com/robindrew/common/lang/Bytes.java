package com.robindrew.common.lang;

public class Bytes {

	public static byte readByte(byte[] array, int offset) {
		return array[offset];
	}

	public static short readShort(byte[] array, int offset) {
		return (short) (((array[offset + 0] & 255) << 8) + ((array[offset + 1] & 255) << 0));
	}

	public static int readInt(byte[] array, int offset) {
		return ((int) (array[offset + 0] & 255) << 24) + ((array[offset + 1] & 255) << 16) + ((array[offset + 2] & 255) << 8) + ((array[offset + 3] & 255) << 0);
	}

	public static long readLong(byte[] array, int offset) {
		return (((long) array[offset + 0] << 56) + ((long) (array[offset + 1] & 255) << 48) + ((long) (array[offset + 2] & 255) << 40) + ((long) (array[offset + 3] & 255) << 32) + ((long) (array[offset + 4] & 255) << 24) + ((array[offset + 5] & 255) << 16) + ((array[offset + 6] & 255) << 8) + ((array[offset + 7] & 255) << 0));
	}

	public static byte[] readBytes(byte[] array, int offset) {
		int length = readInt(array, offset);
		byte[] readBytes = new byte[length];
		System.arraycopy(array, offset + 4, readBytes, 0, length);
		return readBytes;
	}

	public static void writeByte(byte[] array, int offset, byte value) {
		array[offset] = value;
	}

	public static void writeShort(byte[] array, int offset, short value) {
		array[offset + 0] = (byte) ((value >>> 8) & 255);
		array[offset + 1] = (byte) ((value >>> 0) & 255);
	}

	public static void writeInt(byte[] array, int offset, int value) {
		array[offset + 0] = (byte) ((value >>> 24) & 255);
		array[offset + 1] = (byte) ((value >>> 16) & 255);
		array[offset + 2] = (byte) ((value >>> 8) & 255);
		array[offset + 3] = (byte) ((value >>> 0) & 255);
	}

	public static void writeLong(byte[] array, int offset, long value) {
		array[offset + 0] = (byte) (value >>> 56);
		array[offset + 1] = (byte) (value >>> 48);
		array[offset + 2] = (byte) (value >>> 40);
		array[offset + 3] = (byte) (value >>> 32);
		array[offset + 4] = (byte) (value >>> 24);
		array[offset + 5] = (byte) (value >>> 16);
		array[offset + 6] = (byte) (value >>> 8);
		array[offset + 7] = (byte) (value >>> 0);
	}

	public static int writeBytes(byte[] array, int offset, byte[] writeBytes) {
		writeInt(array, offset, writeBytes.length);
		offset += 4;
		System.arraycopy(writeBytes, 0, array, offset, writeBytes.length);
		offset += writeBytes.length;
		return offset;

	}

}
