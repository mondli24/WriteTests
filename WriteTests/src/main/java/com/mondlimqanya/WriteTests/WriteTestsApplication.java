package com.mondlimqanya.WriteTests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.mondlimqanya.WriteTests.entity")
@EnableJpaRepositories("com.mondlimqanya.WriteTests.repository")
@SpringBootApplication
public class WriteTestsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WriteTestsApplication.class, args);
	}

}
