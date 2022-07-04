package com.robindrew.spring.servlet;

import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static javax.servlet.RequestDispatcher.ERROR_REQUEST_URI;
import static javax.servlet.RequestDispatcher.ERROR_STATUS_CODE;

import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.assertj.core.util.Throwables;

import com.robindrew.common.http.response.IHttpResponse;
import com.robindrew.common.http.servlet.request.IHttpRequest;
import com.robindrew.common.http.servlet.template.AbstractTemplateServlet;

/**
 * The error page.
 */
@WebServlet(urlPatterns = "/error")
public class ErrorServlet extends AbstractTemplateServlet {

	@Override
	public String getTemplateName() {
		return "site/Error.html";
	}

	@Override
	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {
		super.execute(request, response, dataMap);

		// Check the status of the error
		Integer status = (Integer) request.getAttribute(ERROR_STATUS_CODE);
		String uri = (String) request.getAttribute(ERROR_REQUEST_URI);

		// Home page redirect (redirects from "/" to "/index")
		if (status == 404 && uri.equals("/")) {
			response.found("/index");
			return;
		}

		dataMap.put("request", request);
		dataMap.put("errorPage", "true");
		dataMap.put("errorStatus", status);
		dataMap.put("errorUri", uri);

		Throwable cause = (Throwable) request.getAttribute(ERROR_EXCEPTION);
		if (cause != null) {
			dataMap.put("errorException", Throwables.getStackTrace(cause));
		}
	}

}
