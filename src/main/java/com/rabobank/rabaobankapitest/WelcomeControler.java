package com.rabobank.rabaobankapitest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeControler {
	
	@GetMapping("/rabo-api")
	public String welcome() {
		return "Hello, all API tests were successful";
	}

}
