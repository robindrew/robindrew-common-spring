package com.robindrew.common.encrypt;

public interface IStringEncoder extends IEncoder {

	String encodeToString(byte[] bytes);

	byte[] decodeFromString(String encoded);

}
