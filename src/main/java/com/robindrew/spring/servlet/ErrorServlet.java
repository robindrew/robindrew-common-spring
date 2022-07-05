package com.robindrew.spring.servlet;

import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static javax.servlet.RequestDispatcher.ERROR_REQUEST_URI;
import static javax.servlet.RequestDispatcher.ERROR_STATUS_CODE;

import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.google.common.base.Throwables;
import com.robindrew.common.http.response.IHttpResponse;
import com.robindrew.common.http.servlet.request.IHttpRequest;
import com.robindrew.spring.component.servlet.template.AbstractTemplateServlet;
import com.robindrew.spring.component.servlet.template.TemplateResource;;

/**
 * The error page.
 */
@WebServlet(urlPatterns = "/error")
@TemplateResource("site/Error.html")
public class ErrorServlet extends AbstractTemplateServlet {

	@Override
	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {
		super.execute(request, response, dataMap);

		// Check the status of the error
		Integer status = (Integer) request.getAttribute(ERROR_STATUS_CODE);
		String uri = (String) request.getAttribute(ERROR_REQUEST_URI);

		dataMap.put("request", request);
		dataMap.put("errorPage", "true");
		dataMap.put("errorStatus", status);
		dataMap.put("errorUri", uri);

		Throwable cause = (Throwable) request.getAttribute(ERROR_EXCEPTION);
		if (cause != null) {
			dataMap.put("errorException", Throwables.getStackTraceAsString(cause));
		}
	}

}
