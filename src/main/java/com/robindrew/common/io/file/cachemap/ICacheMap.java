package com.robindrew.common.io.file.cachemap;

import java.util.Map.Entry;
import java.util.Set;

public interface ICacheMap<K, V> {

	boolean isEmpty();

	int size();

	Set<K> keySet();

	Set<Entry<K, V>> entrySet();

	boolean containsKey(K key);

	void put(K key, V value);

	V get(K key);
	
	void remove(K key);
	
	void clear();

}
