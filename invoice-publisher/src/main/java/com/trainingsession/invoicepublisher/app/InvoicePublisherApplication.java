package com.trainingsession.invoicepublisher.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.trainingsession.invoicepublisher.*")
public class InvoicePublisherApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoicePublisherApplication.class, args);
	}

}
