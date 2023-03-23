package com.robindrew.common.encrypt;

import static com.google.common.base.Charsets.UTF_8;

import java.nio.charset.Charset;

import com.google.common.io.BaseEncoding;

public class HexEncoder implements IStringEncoder {

	private Charset charset = UTF_8;

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		if (charset == null) {
			throw new NullPointerException("charset");
		}
		this.charset = charset;
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
		return BaseEncoding.base16().encode(bytes);
	}

	@Override
	public byte[] decodeFromString(String encoded) {
		return BaseEncoding.base16().decode(encoded);
	}

}
