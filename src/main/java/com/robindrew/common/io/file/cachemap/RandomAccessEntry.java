package com.robindrew.common.io.file.cachemap;

public class RandomAccessEntry {

	private final int offset;
	private volatile String key;
	private volatile int keyLength;
	private volatile int valueLength;

	public RandomAccessEntry(int offset, String key, int keyLength, int valueLength) {
		this.offset = offset;
		this.key = key;
		this.keyLength = keyLength;
		this.valueLength = valueLength;
	}

	public int getOffset() {
		return offset;
	}

	public int getKeyOffset() {
		return offset;
	}

	public int getValueOffset() {
		return offset + 8 + keyLength;
	}

	public String getKey() {
		return key;
	}

	public int getKeyLength() {
		return keyLength;
	}

	public int getValueLength() {
		return valueLength;
	}

	public int getLength() {
		return 8 + keyLength + valueLength;
	}

	public boolean isRemoved() {
		return keyLength == 0;
	}

	public void remove() {
		valueLength += keyLength;
		keyLength = 0;
		key = "";
	}
}
