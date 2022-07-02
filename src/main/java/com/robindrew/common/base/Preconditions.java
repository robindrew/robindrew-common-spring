package com.robindrew.common.base;

import java.util.Collection;
import java.util.Map;

public class Preconditions {

	public static <T> T notNull(String variableName, T object) {
		if (variableName == null) {
			throw new IllegalArgumentException("variableName is null");
		}
		if (variableName.isEmpty()) {
			throw new IllegalArgumentException("variableName is empty");
		}
		if (object == null) {
			throw new NullPointerException(variableName);
		}
		return object;
	}

	public static String notEmpty(String variableName, String text) {
		notNull(variableName, text);
		if (text.isEmpty()) {
			throw new IllegalArgumentException(variableName + " is empty");
		}
		return text;
	}

	public static <C extends Collection<?>> C notEmpty(String variableName, C collection) {
		notNull(variableName, collection);
		if (collection.isEmpty()) {
			throw new IllegalArgumentException(variableName + " is empty");
		}
		return collection;
	}

	public static <M extends Map<?, ?>> M notEmpty(String variableName, M map) {
		notNull(variableName, map);
		if (map.isEmpty()) {
			throw new IllegalArgumentException(variableName + " is empty");
		}
		return map;
	}
}
