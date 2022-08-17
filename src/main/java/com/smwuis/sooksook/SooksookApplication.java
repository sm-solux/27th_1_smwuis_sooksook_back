package com.smwuis.sooksook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SooksookApplication {

	public static void main(String[] args) {
		SpringApplication.run(SooksookApplication.class, args);
	}

}
