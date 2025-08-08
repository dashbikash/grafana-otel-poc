package me.dashbikash.observability.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
	
	@GetMapping("/api/data")
	public String getData(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "Hello "+ name + "!";
	}
}
