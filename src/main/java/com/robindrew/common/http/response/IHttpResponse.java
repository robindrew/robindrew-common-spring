package com.robindrew.common.http.response;

import javax.servlet.http.HttpServletResponse;

import com.google.common.io.ByteSource;

public interface IHttpResponse extends HttpServletResponse {

	void found(String redirect);

	void notFound();

	void internalServerError(String text);

	void ok(String contentType, String text);

	void ok(String contentType, CharSequence text);

	void ok(String contentType, ByteSource source);

	void ok(String contentType);

	void setCookie(String key, String value);

}
