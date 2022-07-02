package com.robindrew.spring.servlet;

import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.robindrew.common.http.response.IHttpResponse;
import com.robindrew.common.http.servlet.request.IHttpRequest;
import com.robindrew.common.http.servlet.template.AbstractTemplateServlet;

@WebServlet(urlPatterns = "/error")
public class IndexPage extends AbstractTemplateServlet {

	@Override
	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {
		super.execute(request, response, dataMap);
	}

	@Override
	public String getTemplateName() {
		return "site/Index.html";
	}

}
