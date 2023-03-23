package com.robindrew.common.encrypt;

public interface IEncryptor {

	byte[] encrypt(byte[] bytes);

	byte[] decrypt(byte[] bytes);

}
