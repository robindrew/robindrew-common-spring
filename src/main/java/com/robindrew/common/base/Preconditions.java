package com.robindrew.common.base;

import java.io.File;
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

	public static File exists(String name, File value) {
		notNull(name, value);
		if (!value.exists()) {
			throw new IllegalArgumentException("'" + name + "' does not exist: '" + value.getAbsolutePath() + "'");
		}
		return value;
	}

	public static File directory(String name, File value) {
		notNull(name, value);
		if (!value.isDirectory()) {
			throw new IllegalArgumentException("'" + name + "' is not a directory: '" + value + "'");
		}
		return value;
	}

	public static File file(String name, File value) {
		notNull(name, value);
		if (!value.isFile()) {
			throw new IllegalArgumentException("'" + name + "' is not a regular file: '" + value + "'");
		}
		return value;
	}

	public static File directory(String name, String value) {
		return directory(name, new File(value));
	}

	public static File file(String name, String value) {
		return file(name, new File(value));
	}

	public static File existsDirectory(String name, File value) {
		exists(name, value);
		directory(name, value);
		return value;
	}

	public static File existsFile(String name, File value) {
		exists(name, value);
		file(name, value);
		return value;
	}

	public static File existsDirectory(String name, String value) {
		return existsDirectory(name, new File(value));
	}

	public static File existsFile(String name, String value) {
		return existsFile(name, new File(value));
	}
}
