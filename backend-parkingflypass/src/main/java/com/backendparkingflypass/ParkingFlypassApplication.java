package com.backendparkingflypass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource("classpath:application.yml")
public class ParkingFlypassApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingFlypassApplication.class, args);
	}

}
