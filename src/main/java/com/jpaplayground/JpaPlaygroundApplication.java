package com.jpaplayground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaAuditing
public class JpaPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaPlaygroundApplication.class, args);
	}

}
