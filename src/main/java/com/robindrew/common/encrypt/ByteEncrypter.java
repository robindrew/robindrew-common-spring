package com.robindrew.common.encrypt;

import java.util.Random;

public class ByteEncrypter {

	public static final int DEFAULT_BUFFER_CAPACITY = 1000000;

	private final int bufferCapacity;

	public ByteEncrypter(int bufferCapacity) {
		this.bufferCapacity = bufferCapacity;
	}

	public ByteEncrypter() {
		this(DEFAULT_BUFFER_CAPACITY);
	}

	private byte[] newBuffer(Random random) {
		byte[] buffer = new byte[bufferCapacity];
		random.nextBytes(buffer);
		return buffer;
	}

	public byte[] decrypt(long seed, byte[] bytes) {
		return decrypt(new Random(seed), bytes, 0, bytes.length);
	}

	public byte[] decrypt(Random random, byte[] bytes, int offset, int length) {

		if (bufferCapacity < length) {
			return decryptWithCheck(random, bytes, offset, length);
		} else {
			return decryptWithoutCheck(random, bytes, offset, length);
		}
	}

	private byte[] decryptWithCheck(Random random, byte[] bytes, int offset, int length) {
		int bufferOffset = 0;
		byte[] buffer = newBuffer(random);

		byte[] encrypted = new byte[length];
		for (int i = 0; i < length; i++) {
			encrypted[i] = decryptByte(bytes[i + offset], buffer[bufferOffset++]);

			// Check: Re-populate the buffer?
			if (bufferOffset == buffer.length) {
				bufferOffset = 0;
				random.nextBytes(buffer);
			}
		}
		return encrypted;
	}

	private byte[] decryptWithoutCheck(Random random, byte[] bytes, int offset, int length) {
		int bufferOffset = 0;
		byte[] buffer = newBuffer(random);

		byte[] encrypted = new byte[length];
		for (int i = 0; i < length; i++) {
			encrypted[i] = decryptByte(bytes[i + offset], buffer[bufferOffset++]);
		}
		return encrypted;
	}

	protected byte decryptByte(int value, int randomByte) {
		return (byte) (value - randomByte);
	}

	public byte[] encrypt(long seed, byte[] bytes) {
		return encrypt(new Random(seed), bytes, 0, bytes.length);
	}

	public byte[] encrypt(Random random, byte[] bytes, int offset, int length) {
		if (bufferCapacity < length) {
			return encryptWithCheck(random, bytes, offset, length);
		} else {
			return encryptWithoutCheck(random, bytes, offset, length);
		}
	}

	private byte[] encryptWithCheck(Random random, byte[] bytes, int offset, int length) {
		int bufferOffset = 0;
		byte[] buffer = newBuffer(random);

		byte[] encrypted = new byte[length];
		for (int i = 0; i < length; i++) {
			encrypted[i] = encryptByte(bytes[i + offset], buffer[bufferOffset++]);

			// Check: Re-populate the buffer?
			if (bufferOffset == buffer.length) {
				bufferOffset = 0;
				random.nextBytes(buffer);
			}
		}
		return encrypted;
	}

	private byte[] encryptWithoutCheck(Random random, byte[] bytes, int offset, int length) {
		int bufferOffset = 0;
		byte[] buffer = newBuffer(random);

		byte[] encrypted = new byte[length];
		for (int i = 0; i < length; i++) {
			encrypted[i] = encryptByte(bytes[i + offset], buffer[bufferOffset++]);
		}
		return encrypted;
	}

	protected byte encryptByte(int value, int randomByte) {
		return (byte) (value + randomByte);
	}

}
