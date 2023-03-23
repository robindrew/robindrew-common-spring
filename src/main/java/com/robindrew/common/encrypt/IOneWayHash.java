package com.robindrew.common.encrypt;

public interface IOneWayHash {

	byte[] hashToBytes(byte[] input);

	String hashToString(byte[] input);

}
