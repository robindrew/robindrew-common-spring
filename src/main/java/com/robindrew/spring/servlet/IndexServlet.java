package com.robindrew.spring.servlet;

import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.robindrew.common.http.response.IHttpResponse;
import com.robindrew.common.http.servlet.request.IHttpRequest;
import com.robindrew.common.http.servlet.template.AbstractTemplateServlet;

/**
 * The index page handles both "/" and error pages to work around the way spring servlets are configured.
 */
@WebServlet(urlPatterns = { "/index", "/home" })
public class IndexServlet extends AbstractTemplateServlet {

	@Override
	public String getTemplateName() {
		return "site/Index.html";
	}

	@Override
	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {
		super.execute(request, response, dataMap);
	}

}
