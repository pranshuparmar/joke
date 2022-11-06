package com.pranshu.joke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class JokeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JokeApplication.class, args);
	}

}
