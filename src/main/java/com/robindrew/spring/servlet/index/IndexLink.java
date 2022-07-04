package com.robindrew.spring.servlet.index;

import static com.robindrew.common.base.Preconditions.notEmpty;

import com.robindrew.common.text.Strings;

public class IndexLink {

	private final String name;
	private final String url;
	private final String color;

	public IndexLink(String name, String url, String color) {
		this.name = notEmpty("name", name);
		this.url = notEmpty("url", url);
		this.color = notEmpty("color", color);
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return getName();
	}

	public String getHtmlName() {
		return getName().replace(' ', '_');
	}

	public String getUrl() {
		return url;
	}

	public String getColor() {
		return color;
	}

	@Override
	public String toString() {
		return Strings.toString(this);
	}

}
