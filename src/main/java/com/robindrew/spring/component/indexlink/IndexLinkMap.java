package com.robindrew.spring.component.indexlink;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class IndexLinkMap implements Iterable<IndexLink> {

	private final Map<String, IndexLink> linkMap = new LinkedHashMap<>();

	public void add(String name, String url, String color) {
		add(new IndexLink(name, url, color));
	}

	public void add(IndexLink link) {
		synchronized (linkMap) {
			linkMap.put(link.getName(), link);
		}
	}

	public List<IndexLink> getLinks() {
		synchronized (linkMap) {
			return new ArrayList<>(linkMap.values());
		}
	}

	@Override
	public Iterator<IndexLink> iterator() {
		return getLinks().iterator();
	}

	@Override
	public String toString() {
		return linkMap.toString();
	}

}
