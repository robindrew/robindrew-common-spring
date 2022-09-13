package com.robindrew.spring.component.status.metric;

import com.robindrew.spring.component.status.Threshold;

public interface IMetric {

	default void update() {
		// Nothing to do
	}

	default Threshold<?> apply(Threshold<?> threshold) {
		return threshold;
	}

	default boolean isValid() {
		return true;
	}

	StringBuilder appendTo(StringBuilder line);

}
