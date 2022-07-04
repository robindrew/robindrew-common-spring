package com.robindrew.spring.component.stats;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableSet;

@Component
public class StatsCache implements IStatsCache {

	@Value("${stats.cache.capacity:8}")
	private int capacity;
	private final Map<String, Deque<IStatsInstant>> cache = new LinkedHashMap<>();

	@Override
	public Set<String> getKeys() {
		synchronized (cache) {
			return ImmutableSet.copyOf(cache.keySet());
		}
	}

	@Override
	public Set<IStatsInstant> getStats(String key) {
		synchronized (cache) {
			Deque<IStatsInstant> instants = cache.get(key);
			if (instants == null) {
				return Collections.emptySet();
			}
			return ImmutableSet.copyOf(instants);
		}
	}

	@Override
	public void put(IStatsInstant instant) {
		String key = instant.getKey();
		synchronized (cache) {
			Deque<IStatsInstant> instants = cache.get(key);
			if (instants == null) {
				instants = new LinkedList<>();
				cache.put(key, instants);
			}
			instants.addLast(instant);
			if (instants.size() > capacity) {
				instants.removeFirst();
			}
		}
	}

}
