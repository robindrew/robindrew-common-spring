package com.robindrew.common.encrypt;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class JasyptEncryptorFactory {

	public StringEncryptor createDefault(String password) {
		// Creates and returns the default encryptor used by Spring
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
		config.setPassword(password);
		config.setKeyObtentionIterations(1000);
		config.setPoolSize(1);
		config.setSaltGeneratorClassName(org.jasypt.salt.RandomSaltGenerator.class.getName());
		config.setIvGeneratorClassName(org.jasypt.iv.RandomIvGenerator.class.getName());
		config.setStringOutputType("base64");
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
