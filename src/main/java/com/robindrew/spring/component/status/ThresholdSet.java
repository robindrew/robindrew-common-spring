package com.robindrew.spring.component.status;

import java.util.LinkedHashSet;
import java.util.Set;

public class ThresholdSet<V> {

	private final Set<Threshold<V>> set = new LinkedHashSet<>();

	public void add(Threshold<V> threshold) {
		set.add(threshold);
	}

	public Threshold<?> merge(V value, Threshold<?> threshold) {
		for (Threshold<V> element : set) {
			if (element.matches(value)) {
				threshold = threshold.merge(element);
			}
		}
		return threshold;
	}

}
