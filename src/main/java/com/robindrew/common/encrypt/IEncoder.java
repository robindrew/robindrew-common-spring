package com.robindrew.common.encrypt;

public interface IEncoder {

	byte[] encodeBytes(byte[] bytes);

	byte[] decodeBytes(byte[] bytes);

}
