package com.trainingsession.invoiceconsumer.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.trainingsession.invoiceconsumer.*")
public class InvoiceConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceConsumerApplication.class, args);
	}

}
