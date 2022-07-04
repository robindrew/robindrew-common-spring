package com.robindrew.spring.component.stats;

import java.util.Set;

public interface IStatsCache {

	Set<String> getKeys();

	Set<IStatsInstant> getStats(String key);

	void put(IStatsInstant instant);

}
