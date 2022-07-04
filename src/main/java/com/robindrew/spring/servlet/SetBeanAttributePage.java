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
import com.robindrew.common.text.parser.IStringParser;
import com.robindrew.common.text.parser.StringParserMap;
import com.robindrew.spring.servlet.bean.BeanAttributeView;

@WebServlet(urlPatterns = "/SetBeanAttribute")
@TemplateResource("site/SetBeanAttribute.html")
public class SetBeanAttributePage extends AbstractTemplateServlet {

	@Override
	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {
		super.execute(request, response, dataMap);

		String domain = request.getString("domain");
		String type = request.getString("type");
		String name = request.getString("name");
		String attributeName = request.getString("attribute");
		String newValue = request.getString("value");

		BeanServer server = new BeanServer();
		IBean bean = server.getBean(domain, type, name);
		IBeanAttribute attribute = bean.getAttribute(attributeName);

		Object value = parseValue(newValue, attribute);
		attribute.setValue(value);

		value = attribute.getValue();
		dataMap.put("bean", bean);
		dataMap.put("attribute", new BeanAttributeView(attribute));

		// String
		if (value instanceof String || value instanceof ObjectName) {
			dataMap.put("valueType", "String");
			dataMap.put("value", value);
			return;
		}

		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// String json = gson.toJson(value);
		// dataMap.put("valueType", "Json");
		// dataMap.put("value", json);
	}

	private Object parseValue(String value, IBeanAttribute attribute) {
		Class<?> type = attribute.getType();
		StringParserMap map = new StringParserMap();
		IStringParser<?> parser = map.getParser(type);
		return parser.parse(value);
	}
}
