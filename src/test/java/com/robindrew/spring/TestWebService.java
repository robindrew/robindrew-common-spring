package com.robindrew.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestWebService extends BaseSpringService {

	public static void main(String[] args) {
		SpringApplication.run(TestWebService.class, args);
	}

}
