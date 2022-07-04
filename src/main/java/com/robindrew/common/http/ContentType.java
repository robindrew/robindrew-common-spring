package com.robindrew.common.http;

import java.nio.charset.Charset;

import com.google.common.base.Charsets;

public class ContentType {

	/** text/plain; charset=UTF-8 */
	public static final String TEXT_PLAIN_UTF8 = get(MimeType.TEXT_PLAIN, Charsets.UTF_8);
	/** text/html; charset=UTF-8 */
	public static final String TEXT_HTML_UTF8 = get(MimeType.TEXT_HTML, Charsets.UTF_8);

	public static String get(String mimeType, Charset charset) {
		if (charset == null) {
			return mimeType;
		}
		return mimeType + "; encoding=" + charset;
	}

}
