package com.robindrew.common.encrypt;

import static com.google.common.base.Charsets.UTF_8;

import java.nio.charset.Charset;

import com.google.common.io.BaseEncoding;

public class Base64Encoder implements IStringEncoder {

	private Charset charset = UTF_8;
	private boolean urlEncoding = false;

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		if (charset == null) {
			throw new NullPointerException("charset");
		}
		this.charset = charset;
	}

	public boolean isUrlEncoding() {
		return urlEncoding;
	}

	public void setUrlEncoding(boolean urlEncoding) {
		this.urlEncoding = urlEncoding;
	}

	private BaseEncoding getEncoding() {
		if (urlEncoding) {
			return BaseEncoding.base64Url();
		} else {
			return BaseEncoding.base64();
		}
	}

	@Override
	public byte[] encodeBytes(byte[] bytes) {
		return encodeToString(bytes).getBytes(charset);
	}

	@Override
	public byte[] decodeBytes(byte[] bytes) {
		return decodeFromString(new String(bytes, charset));
	}

	@Override
	public String encodeToString(byte[] bytes) {
		return getEncoding().encode(bytes);
	}

	@Override
	public byte[] decodeFromString(String encoded) {
		return getEncoding().decode(encoded);
	}

}
