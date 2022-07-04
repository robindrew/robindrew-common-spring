package com.robindrew.spring.servlet;

import java.util.Map;

import javax.management.ObjectName;
import javax.servlet.annotation.WebServlet;

import com.robindrew.common.http.response.IHttpResponse;
import com.robindrew.common.http.servlet.request.IHttpRequest;
import com.robindrew.common.http.servlet.template.AbstractTemplateServlet;
import com.robindrew.common.http.servlet.template.TemplateResource;
import com.robindrew.common.mbean.model.BeanServer;
import com.robindrew.common.mbean.model.IBean;
import com.robindrew.common.mbean.model.IBeanAttribute;
import com.robindrew.common.text.Strings;
import com.robindrew.spring.servlet.bean.BeanAttributeView;

@WebServlet(urlPatterns = "/GetBeanAttribute")
@TemplateResource("site/GetBeanAttribute.html")
public class GetBeanAttributePage extends AbstractTemplateServlet {

	@Override
	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {
		super.execute(request, response, dataMap);

		String domain = request.getString("domain");
		String type = request.getString("type");
		String name = request.getString("name");
		String attributeName = request.getString("attribute");

		BeanServer server = new BeanServer();
		IBean bean = server.getBean(domain, type, name);
		IBeanAttribute attribute = bean.getAttribute(attributeName);

		Object value = attribute.getValue();
		dataMap.put("bean", bean);
		dataMap.put("attribute", new BeanAttributeView(attribute));

		// String
		if (value instanceof String || value instanceof ObjectName) {
			dataMap.put("valueType", "String");
			dataMap.put("value", value);
			return;
		}

		String json = Strings.json(value, true);
		dataMap.put("valueType", "Json");
		dataMap.put("value", json);
	}
}
