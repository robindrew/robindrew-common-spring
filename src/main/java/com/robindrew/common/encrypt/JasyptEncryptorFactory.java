package com.robindrew.common.encrypt;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class JasyptEncryptorFactory {

	private static final int DEFAULT_HASHING_ITERATIONS = 1000;
	private static final String DEFAULT_OUTPUT_TYPE = "base64";
	private static final String DEFAULT_ALGORITHM = "PBEWITHHMACSHA512ANDAES_256";

	public StringEncryptor createDefault(String password) {
		// Creates and returns the default encryptor used by Spring
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setAlgorithm(DEFAULT_ALGORITHM);
		config.setPassword(password);
		config.setKeyObtentionIterations(DEFAULT_HASHING_ITERATIONS);
		config.setPoolSize(1);
		config.setSaltGeneratorClassName(org.jasypt.salt.RandomSaltGenerator.class.getName());
		config.setIvGeneratorClassName(org.jasypt.iv.RandomIvGenerator.class.getName());
		config.setStringOutputType(DEFAULT_OUTPUT_TYPE);
		encryptor.setConfig(config);
		encryptor.initialize();
		return encryptor;
	}

	public static void main(String[] args) throws Throwable {
		String password = args[0];
		String text = args[1];

		JasyptEncryptorFactory factory = new JasyptEncryptorFactory();
		StringEncryptor encryptor = factory.createDefault(password);

		String encrypted = encryptor.encrypt(text);
		System.out.println(encrypted);
	}
}
