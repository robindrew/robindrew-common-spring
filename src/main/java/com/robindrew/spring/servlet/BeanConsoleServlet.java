package com.robindrew.spring.servlet;

import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.robindrew.common.http.response.IHttpResponse;
import com.robindrew.common.http.servlet.request.IHttpRequest;
import com.robindrew.common.mbean.model.BeanServer;
import com.robindrew.spring.component.servlet.template.AbstractTemplateServlet;
import com.robindrew.spring.component.servlet.template.TemplateResource;

@WebServlet(urlPatterns = "/BeanConsole")
@TemplateResource("site/BeanConsole.html")
public class BeanConsoleServlet extends AbstractTemplateServlet {

	@Override
	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {
		super.execute(request, response, dataMap);

		BeanServer server = new BeanServer();
		dataMap.put("standardBeans", server.getBeans(true));
		dataMap.put("customBeans", server.getBeans(false));
	}
}
