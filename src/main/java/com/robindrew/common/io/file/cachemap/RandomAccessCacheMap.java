package com.robindrew.common.io.file.cachemap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.robindrew.common.base.Java;
import com.robindrew.common.lang.Bytes;

public class RandomAccessCacheMap implements ICacheMap<String, byte[]> {

	private int nextOffset = 0;
	private final RandomAccessFile file;
	private final Map<String, RandomAccessEntry> entryMap = new LinkedHashMap<>();

	public RandomAccessCacheMap(File file) throws FileNotFoundException {
		this.file = new RandomAccessFile(file, "rw");
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int size() {
		synchronized (entryMap) {
			return entryMap.size();
		}
	}

	@Override
	public Set<String> keySet() {
		synchronized (entryMap) {
			return ImmutableSet.copyOf(entryMap.keySet());
		}
	}

	@Override
	public boolean containsKey(String key) {
		synchronized (entryMap) {
			return entryMap.containsKey(key);
		}
	}

	@Override
	public void put(String key, byte[] value) {
		if (key.isEmpty()) {
			throw new IllegalArgumentException("key is empty");
		}
		if (value == null) {
			throw new NullPointerException("value");
		}

		byte[] keyBytes = key.getBytes(Charsets.UTF_8);

		// Populate the buffer
		int entryLength = 4 + keyBytes.length + 4 + value.length;

		byte[] buffer = new byte[entryLength];
		int offset = 0;
		offset = Bytes.writeBytes(buffer, offset, keyBytes);
		offset = Bytes.writeBytes(buffer, offset, value);

		try {
			synchronized (file) {
				if (getEntry(key) != null) {
					throw new IllegalArgumentException("Entry exists for key: '" + key + "'");
				}

				file.seek(nextOffset);
				file.write(buffer);
				buffer = null;

				// Add new entry
				RandomAccessEntry entry = new RandomAccessEntry(nextOffset, key, keyBytes.length, value.length);
				synchronized (entryMap) {
					entryMap.put(key, entry);
				}

				// Update offset
				nextOffset += entryLength;
			}
		} catch (Exception e) {
			throw Java.propagate(e);
		}
	}

	private RandomAccessEntry getEntry(String key) {
		synchronized (entryMap) {
			return entryMap.get(key);
		}
	}

	@Override
	public void remove(String key) {
		if (key.isEmpty()) {
			throw new IllegalArgumentException("key is empty");
		}

		RandomAccessEntry entry = getEntry(key);
		if (entry == null) {
			throw new IllegalArgumentException("No entry exists for key: '" + key + "'");
		}
		if (entry.isRemoved()) {
			return;
		}

		byte[] buffer = new byte[entry.getLength()];

		try {
			synchronized (file) {
				if (entry.isRemoved()) {
					return;
				}

				file.seek(entry.getOffset());
				file.write(buffer);
				buffer = null;

				// Remove the entry
				entry.remove();
			}
		} catch (Exception e) {
			throw Java.propagate(e);
		}

	}

	@Override
	public byte[] get(String key) {
		if (key.isEmpty()) {
			throw new IllegalArgumentException("key is empty");
		}

		RandomAccessEntry entry = getEntry(key);
		if (entry == null) {
			throw new IllegalArgumentException("No entry exists for key: '" + key + "'");
		}
		if (entry.isRemoved()) {
			throw new IllegalArgumentException("Entry removed for key: '" + key + "'");
		}

		byte[] value = new byte[entry.getValueLength()];

		try {
			synchronized (file) {
				if (entry.isRemoved()) {
					throw new IllegalArgumentException("Entry removed for: '" + key + "'");
				}

				file.seek(entry.getValueOffset());
				file.read(value);
			}
		} catch (Exception e) {
			throw Java.propagate(e);
		}

		return value;
	}

	@Override
	public Set<Entry<String, byte[]>> entrySet() {
		synchronized (entryMap) {
			Builder<Entry<String, byte[]>> set = ImmutableSet.builder();
			for (RandomAccessEntry entry : entryMap.values()) {
				set.add(new MapEntry(entry));
			}
			return set.build();
		}
	}

	@Override
	public void clear() {
		synchronized (file) {
			synchronized (entryMap) {
				entryMap.clear();
			}
			nextOffset = 0;
			try {
				file.setLength(0);
			} catch (Exception e) {
				throw Java.propagate(e);
			}
		}
	}

	class MapEntry implements Entry<String, byte[]> {

		private final RandomAccessEntry entry;

		public MapEntry(RandomAccessEntry entry) {
			this.entry = entry;
		}

		@Override
		public String getKey() {
			return entry.getKey();
		}

		@Override
		public byte[] getValue() {
			return get(entry.getKey());
		}

		@Override
		public byte[] setValue(byte[] value) {
			throw new UnsupportedOperationException();
		}

	}

}
