package com.robindrew.spring.component.velocity;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import com.robindrew.common.http.ContentType;
import com.robindrew.common.http.servlet.Servlets;
import com.robindrew.common.template.ITemplate;
import com.robindrew.common.template.ITemplateLocator;
import com.robindrew.common.template.TemplateData;

public class VelocityView implements View {

	private final String templateName;
	private final ITemplateLocator locator;
	private final String prefix;
	private final String postfix;

	public VelocityView(String templateName, ITemplateLocator locator, String prefix, String postfix) {
		this.templateName = templateName;
		this.locator = locator;
		this.prefix = prefix;
		this.postfix = postfix;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ITemplate template = createTemplate();
		String html = template.execute(new TemplateData(model));
		Servlets.ok(response, html, ContentType.TEXT_HTML_UTF8);
	}

	public boolean isValid() {
		try {
			createTemplate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public ITemplate createTemplate() {
		return locator.getTemplate(getTemplatePath());
	}

	private String getTemplatePath() {
		StringBuilder name = new StringBuilder();
		if (prefix != null && !templateName.startsWith(prefix)) {
			name.append(prefix);
		}
		name.append(templateName);
		if (postfix != null && !templateName.endsWith(postfix)) {
			name.append(postfix);
		}
		return name.toString();
	}

	@Override
	public String getContentType() {
		return "text/html";
	}

}
