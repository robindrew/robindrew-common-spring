package com.robindrew.common.http.servlet;

import static com.robindrew.common.http.ContentType.TEXT_HTML_UTF8;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.robindrew.common.base.Java;
import com.robindrew.common.base.Preconditions;
import com.robindrew.common.http.ContentType;
import com.robindrew.common.http.servlet.header.HttpHeader;

/**
 * The servlets utility.
 */
public class Servlets {

	private static volatile Charset defaultCharset = Charsets.UTF_8;

	public static Charset getDefaultCharset() {
		return defaultCharset;
	}

	public static void setDefaultCharset(Charset charset) {
		defaultCharset = Preconditions.notNull("charset", charset);
	}

	/**
	 * HTTP Response: 401 / Unauthorized (authenticate)
	 */
	public static void unauthorized(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.addHeader(HttpHeader.WWW_AUTHENTICATE.get(), "Basic");
	}

	/**
	 * HTTP Response: 403 / Forbidden
	 */
	public static void forbidden(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	}

	/**
	 * HTTP Response: 302 / Found (redirect)
	 */
	public static void found(HttpServletResponse response, String path) {
		response.setStatus(HttpServletResponse.SC_FOUND);
		response.addHeader(HttpHeader.LOCATION.get(), path);
	}

	/**
	 * HTTP Response: 404 / Not Found
	 */
	public static final void notFound(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}

	/**
	 * HTTP Response: 400 / Bad Request
	 */
	public static final void badRequest(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

	/**
	 * HTTP Response: 404 / Not Found
	 */
	public static final void notFound(HttpServletResponse response, String path) {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		setContent(response, "<h1>Error 404</h1><code>Not Found: \"" + path + "\"</code>", TEXT_HTML_UTF8);
	}

	/**
	 * HTTP Response: 500 / Internal Server Error
	 */
	public static final void internalServerError(HttpServletResponse response, String text) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		setContent(response, text, ContentType.TEXT_PLAIN_UTF8);
	}

	/**
	 * HTTP Response: 200 / OK
	 */
	public static final void ok(HttpServletResponse response, String text, String contentType) {
		response.setStatus(HttpServletResponse.SC_OK);
		setContent(response, text, contentType);
	}

	/**
	 * HTTP Response: 200 / OK
	 */
	public static final void ok(HttpServletResponse response, String contentType) {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(contentType);
	}

	/**
	 * HTTP Response: 200 / OK
	 */
	public static final void ok(HttpServletResponse response, ByteSource source, String contentType) {
		response.setStatus(HttpServletResponse.SC_OK);
		setContent(response, source, contentType);
	}

	public static void setContent(HttpServletResponse response, String text, String contentType) {
		Charset charset = parseCharset(contentType);
		byte[] bytes = text.getBytes(charset);
		setContent(response, bytes, contentType);
	}

	private static Charset parseCharset(String contentType) {
		if (contentType.endsWith("UTF-8")) {
			return Charsets.UTF_8;
		}
		int index = contentType.indexOf("charset=");
		if (index == -1) {
			return defaultCharset;
		}
		index += "charset=".length();
		return Charset.forName(contentType.substring(index));
	}

	public static void setContent(HttpServletResponse response, byte[] bytes, String contentType) {
		Preconditions.notNull("contentType", contentType);

		response.setContentType(contentType);
		writeContent(response, bytes);
	}

	public static void setContent(HttpServletResponse response, ByteSource source, String contentType) {
		Preconditions.notNull("contentType", contentType);

		response.setContentType(contentType);
		writeContent(response, source);
	}

	public static void writeContent(HttpServletResponse response, byte[] bytes) {
		try {
			ServletOutputStream output = response.getOutputStream();
			output.write(bytes);
			output.flush();
		} catch (IOException e) {
			throw Java.propagate(e);
		}
	}

	public static void writeContent(HttpServletResponse response, ByteSource source) {
		try {
			ServletOutputStream output = response.getOutputStream();
			source.copyTo(output);
			output.flush();
		} catch (IOException e) {
			throw Java.propagate(e);
		}
	}

	public static void setCookie(HttpServletResponse response, String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
