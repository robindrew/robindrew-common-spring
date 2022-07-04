package com.robindrew.common.http.servlet.template;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Stopwatch;
import com.robindrew.common.base.Java;
import com.robindrew.common.http.response.IHttpResponse;
import com.robindrew.common.http.servlet.AbstractBaseServlet;
import com.robindrew.common.http.servlet.request.IHttpRequest;
import com.robindrew.common.template.ITemplate;
import com.robindrew.common.template.ITemplateLocator;
import com.robindrew.common.template.TemplateData;
import com.robindrew.common.text.Strings;
import com.robindrew.spring.component.service.ServiceDefinition;

public abstract class AbstractTemplateServlet extends AbstractBaseServlet {

	public static final String META_RENDER_TIMER = "META_RENDER_TIMER";
	public static final String META_EXECUTE_TIMER = "META_EXECUTE_TIMER";

	@Autowired
	private ITemplateLocator locator;
	@Autowired
	private ServiceDefinition service;

	protected String getTemplateName() {
		return getTemplateResource().value();
	}

	protected String getContentType() {
		return getTemplateResource().contentType();
	}

	private TemplateResource getTemplateResource() {
		TemplateResource[] resource = getClass().getAnnotationsByType(TemplateResource.class);
		if (resource == null || resource.length == 0) {
			throw new IllegalStateException("Required @TemplateResource annotation not found for " + getClass());
		}
		return resource[0];
	}

	protected void execute(IHttpRequest request, IHttpResponse response) {
		Stopwatch renderTimer = Stopwatch.createStarted();

		// Find the template
		ITemplate template = locator.getTemplate(getTemplateName());

		// Handle the request
		Map<String, Object> dataMap = new LinkedHashMap<>();
		Stopwatch executeTimer = Stopwatch.createStarted();
		execute(request, response, dataMap);
		executeTimer.stop();

		// Found (Redirect)?
		if (response.getStatus() == HttpServletResponse.SC_FOUND) {
			return;
		}

		// Add meta data to the data map
		dataMap.put(META_EXECUTE_TIMER, executeTimer);
		dataMap.put(META_RENDER_TIMER, renderTimer);

		// Execute the template
		String text = template.execute(new TemplateData(dataMap));
		response.ok(getContentType(), text);
	}

	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {

		dataMap.put("title", service.getName() + " #" + service.getInstance());

		dataMap.put("serviceName", service.getName());
		dataMap.put("serviceInstance", service.getInstance());
		dataMap.put("serviceEnv", service.getEnv());
		dataMap.put("servicePort", service.getPort());

		dataMap.put("systemHost", Java.getHostName());
		dataMap.put("systemAddress", Java.getHostAddress());
		dataMap.put("systemTime", Strings.date(Java.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));

		dataMap.put("processId", Java.getProcessId());

		dataMap.put("javaStartTime", Strings.date(Java.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
		dataMap.put("javaUptime", Strings.time(Java.getUptime()));
		dataMap.put("javaMaxMemory", Strings.bytes(Java.maxMemory()));
		dataMap.put("javaUsedMemory", Strings.bytes(Java.usedMemory()));
		dataMap.put("javaPercentMemory", Strings.percent(Java.usedMemory(), Java.maxMemory()));
	}

}
