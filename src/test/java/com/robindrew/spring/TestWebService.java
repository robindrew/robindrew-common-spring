package com.robindrew.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.robindrew.spring.AbstractSpringService;

@SpringBootApplication
public class TestWebService extends AbstractSpringService {

	public static void main(String[] args) {
		SpringApplication.run(TestWebService.class, args);
	}

}
