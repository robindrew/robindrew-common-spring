package com.robindrew.spring.stats;

import java.util.Set;

public interface IStatsCache {

	Set<String> getKeys();

	Set<IStatsInstant> getStats(String key);

	void put(IStatsInstant instant);

}
