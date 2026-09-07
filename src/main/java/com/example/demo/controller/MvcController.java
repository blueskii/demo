package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcController {

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/board")
	public String board() {
		return "board/list";
	}
}