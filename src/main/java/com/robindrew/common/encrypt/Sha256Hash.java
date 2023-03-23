package com.robindrew.common.encrypt;

import java.security.MessageDigest;

import com.robindrew.common.base.Java;

public class Sha256Hash extends StringHash {

	private final MessageDigest digest;

	public Sha256Hash() {
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (Exception e) {
			throw Java.propagate(e);
		}
	}

	@Override
	public byte[] hashToBytes(byte[] input) {
		return digest.digest(input);
	}
}
