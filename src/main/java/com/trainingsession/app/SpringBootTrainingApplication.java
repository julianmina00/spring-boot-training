package com.trainingsession.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.trainingsession.*")
public class SpringBootTrainingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTrainingApplication.class, args);
	}

}
