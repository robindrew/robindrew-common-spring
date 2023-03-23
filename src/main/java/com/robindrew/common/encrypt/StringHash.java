package com.robindrew.common.encrypt;

import com.google.common.io.BaseEncoding;

public abstract class StringHash implements IOneWayHash {

	private BaseEncoding encoding = BaseEncoding.base64();

	public BaseEncoding getEncoding() {
		return encoding;
	}

	public void base64Encoding() {
		encoding = BaseEncoding.base64();
	}

	public void hexEncoding() {
		encoding = BaseEncoding.base16();
	}

	public void setEncoding(BaseEncoding encoding) {
		if (encoding == null) {
			throw new NullPointerException("encoding");
		}
		this.encoding = encoding;
	}

	@Override
	public String hashToString(byte[] input) {
		input = hashToBytes(input);
		return encoding.encode(input);
	}
}
