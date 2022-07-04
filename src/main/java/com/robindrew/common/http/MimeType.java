package com.robindrew.common.http;

public class MimeType {

	public static final String TEXT_PLAIN = "text/plain";
	public static final String TEXT_HTML = "text/html";
	public static final String TEXT_CSS = "text/css";
	public static final String TEXT_XML = "text/xml";
	public static final String TEXT_CSV = "text/csv";
	public static final String TEXT_RTF = "text/rtf";

	public static final String IMAGE_ICON = "image/x-icon";
	public static final String IMAGE_JPEG = "image/jpeg";
	public static final String IMAGE_PNG = "image/png";
	public static final String IMAGE_GIF = "image/gif";

	public static final String VIDEO_MPEG = "video/mpeg";
	public static final String VIDEO_MP4 = "video/mp4";

	public static final String FONT_OTF = "font/otf";

	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_JAVASCRIPT = "application/javascript";

	public static String forExtension(String extension) {
		int dotIndex = extension.lastIndexOf('.');
		if (dotIndex != -1) {
			extension = extension.substring(dotIndex + 1);
		}
		extension = extension.toLowerCase();

		switch (extension) {
			case "txt":
				return TEXT_PLAIN;
			case "html":
			case "htm":
				return TEXT_HTML;
			case "css":
				return TEXT_CSS;
			case "xml":
				return TEXT_XML;
			case "csv":
				return TEXT_CSV;
			case "rtf":
				return TEXT_RTF;
			case "ico":
				return IMAGE_ICON;
			case "jpeg":
			case "jpg":
				return IMAGE_JPEG;
			case "png":
				return IMAGE_PNG;
			case "mpeg":
				return VIDEO_MPEG;
			case "mp4":
				return VIDEO_MP4;
			case "otf":
				return FONT_OTF;
			case "json":
				return APPLICATION_JSON;
			case "js":
				return APPLICATION_JAVASCRIPT;
			default:
				throw new IllegalArgumentException("Extension not mapped: " + extension);
		}

	}

}
