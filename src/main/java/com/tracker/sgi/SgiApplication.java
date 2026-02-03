package com.tracker.sgi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class SgiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgiApplication.class, args);
	}

}
