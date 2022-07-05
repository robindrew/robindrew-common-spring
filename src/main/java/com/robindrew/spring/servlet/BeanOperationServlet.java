package com.robindrew.spring.servlet;

import java.util.List;
import java.util.Map;

import javax.management.ObjectName;
import javax.servlet.annotation.WebServlet;

import com.robindrew.common.http.response.IHttpResponse;
import com.robindrew.common.http.servlet.request.IHttpRequest;
import com.robindrew.common.mbean.model.BeanServer;
import com.robindrew.common.mbean.model.IBean;
import com.robindrew.common.mbean.model.IBeanOperation;
import com.robindrew.common.mbean.model.IBeanParameter;
import com.robindrew.common.text.Strings;
import com.robindrew.common.text.parser.IStringParser;
import com.robindrew.common.text.parser.StringParserMap;
import com.robindrew.spring.component.servlet.template.AbstractTemplateServlet;
import com.robindrew.spring.component.servlet.template.TemplateResource;
import com.robindrew.spring.servlet.bean.BeanOperationView;

@WebServlet(urlPatterns = "/BeanOperation")
@TemplateResource("site/BeanOperation.html")
public class BeanOperationServlet extends AbstractTemplateServlet {

	@Override
	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {
		super.execute(request, response, dataMap);

		String domain = request.getString("domain");
		String type = request.getString("type");
		String name = request.getString("name");
		int operationIndex = request.getInteger("operation");

		BeanServer server = new BeanServer();
		IBean bean = server.getBean(domain, type, name);
		IBeanOperation operation = bean.getOperation(operationIndex);

		BeanOperationView view = new BeanOperationView(operation, operationIndex);
		dataMap.put("bean", bean);
		dataMap.put("operation", view);
		dataMap.put("returnType", view.getReturnTypeName());

		Object value;
		Object[] parameters = getParameters(operation, request);
		try {
			value = operation.invoke(parameters);
		} catch (Exception e) {
			value = Strings.getStackTrace(e);
			dataMap.put("operationFailed", true);
			dataMap.put("valueType", "String");
			dataMap.put("value", value);
			return;
		}

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

	private Object[] getParameters(IBeanOperation operation, IHttpRequest request) {
		List<IBeanParameter> parameterList = operation.getParameters();
		Object[] parameters = new Object[parameterList.size()];
		for (int i = 0; i < parameterList.size(); i++) {
			IBeanParameter parameter = parameterList.get(i);
			String key = "param" + i + "-" + parameter.getName();
			String value = request.getString(key);
			parameters[i] = parseValue(value, parameter);
		}
		return parameters;
	}

	private Object parseValue(String value, IBeanParameter parameter) {
		Class<?> type = parameter.getType();
		StringParserMap map = new StringParserMap();
		IStringParser<?> parser = map.getParser(type);
		return parser.parse(value);
	}
}
