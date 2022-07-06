package com.robindrew.spring.servlet;

import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.robindrew.common.http.response.IHttpResponse;
import com.robindrew.common.http.servlet.request.IHttpRequest;
import com.robindrew.common.mbean.model.BeanServer;
import com.robindrew.common.mbean.model.IBean;
import com.robindrew.spring.component.servlet.template.AbstractTemplateServlet;
import com.robindrew.spring.component.servlet.template.TemplateResource;
import com.robindrew.spring.servlet.bean.BeanAttributeView;
import com.robindrew.spring.servlet.bean.BeanOperationView;

@WebServlet(urlPatterns = "/BeanView")
@TemplateResource("templates/service/BeanView.html")
public class BeanViewServlet extends AbstractTemplateServlet {

	@Override
	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {
		super.execute(request, response, dataMap);

		String domain = request.getString("domain");
		String type = request.getString("type");
		String name = request.getString("name");

		BeanServer server = new BeanServer();
		IBean bean = server.getBean(domain, type, name);
		dataMap.put("bean", bean);
		dataMap.put("attributes", BeanAttributeView.getAttributes(bean));
		dataMap.put("operations", BeanOperationView.getOperations(bean));
	}
}
