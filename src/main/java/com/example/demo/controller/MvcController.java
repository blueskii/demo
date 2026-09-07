package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcController {

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/notices")
	public String notices() {
		return "notices/list";
	}
}