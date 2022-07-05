package com.robindrew.spring.component.velocity;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import com.robindrew.common.template.ITemplateLocator;

@Component
public class VelocityViewResolver extends AbstractCachingViewResolver implements Ordered {

	@Value("${velocity.template.prefix:templates/}")
	private String prefix;
	@Value("${velocity.template.postfix:.html}")
	private String postfix;
	@Value("${velocity.view.resolver.order:0}")
	private int order;
	@Autowired
	private ITemplateLocator locator;
	private final Map<String, VelocityView> viewMap = new LinkedHashMap<>();

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	protected View loadView(String viewName, Locale locale) throws Exception {
		synchronized (viewMap) {
			VelocityView view = viewMap.get(viewName);
			if (view == null) {
				view = loadView(viewName);
			}
			return view;
		}
	}

	private VelocityView loadView(String viewName) {
		VelocityView view = new VelocityView(viewName, locator, prefix, postfix);
		if (!view.isValid()) {
			return null;
		}
		viewMap.put(viewName, view);
		return view;
	}

}
