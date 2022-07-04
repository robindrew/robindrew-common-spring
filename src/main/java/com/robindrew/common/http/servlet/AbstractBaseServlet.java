package com.robindrew.common.http.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.robindrew.common.http.response.HttpResponse;
import com.robindrew.common.http.response.IHttpResponse;
import com.robindrew.common.http.servlet.request.HttpRequest;
import com.robindrew.common.http.servlet.request.IHttpRequest;

public abstract class AbstractBaseServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		execute(new HttpRequest(request), new HttpResponse(response));
	}

	protected abstract void execute(IHttpRequest request, IHttpResponse response);

}
