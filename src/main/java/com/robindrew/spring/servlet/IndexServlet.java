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
 * The index page handles both "/" and error pages to work around the way spring servlets are configured.
 */
@WebServlet(urlPatterns = "/error")
public class IndexServlet extends AbstractTemplateServlet {

	@Override
	public String getTemplateName() {
		return "site/Index.html";
	}

	@Override
	protected void execute(IHttpRequest request, IHttpResponse response, Map<String, Object> dataMap) {
		super.execute(request, response, dataMap);

		// Handle Error
		if (handleError(request, dataMap)) {
			return;
		}
	}

	/**
	 * Handle an error if necessary.
	 */
	private boolean handleError(IHttpRequest request, Map<String, Object> dataMap) {

		// Check the status of the error
		Integer status = (Integer) request.getAttribute(ERROR_STATUS_CODE);
		String uri = (String) request.getAttribute(ERROR_REQUEST_URI);

		// This is the standard state
		if (status == 404 && uri.equals("/")) {
			return false;
		}

		dataMap.put("request", request);
		dataMap.put("errorPage", "true");
		dataMap.put("errorStatus", status);
		dataMap.put("errorUri", uri);

		Throwable cause = (Throwable) request.getAttribute(ERROR_EXCEPTION);
		if (cause != null) {
			dataMap.put("errorException", Throwables.getStackTrace(cause));
		}
		return true;
	}

}
