package com.robindrew.common.encrypt;

import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

public class ByteEncrypterTest {

	private static final Logger log = LoggerFactory.getLogger(ByteEncrypterTest.class);

	@Test
	public void encryptDecryptTest() {

		int size = 10000000;
		int seed = size;

		byte[] bytes = new byte[size];
		Random random = new Random(1999);
		random.nextBytes(bytes);

		ByteEncrypter encrypter = new ByteEncrypter(size);

		Stopwatch timer1 = Stopwatch.createStarted();
		byte[] encrypted = encrypter.encrypt(seed, bytes);
		timer1.stop();

		Stopwatch timer2 = Stopwatch.createStarted();
		byte[] decrypted = encrypter.decrypt(seed, encrypted);
		timer2.stop();

		Assert.assertFalse(Arrays.equals(bytes, encrypted));
		Assert.assertTrue(Arrays.equals(bytes, decrypted));

		log.info("{} bytes encrypted in {}", size, timer1);
		log.info("{} bytes decrypted in {}", size, timer2);
	}

}
