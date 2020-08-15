package com.rabobank.rabaobankapitest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RabaobankApiTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabaobankApiTestApplication.class, args);
		
		WireMock wm = new WireMock();
		wm.start();
		

	}

}
