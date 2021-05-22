package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	// localhost:8080/
	// localhost:8080
	@GetMapping({"","/"})
	public String index() {
		// 머스테치 Default src/main/resources/
		// View Resolver : templates(prefix), mustache (suffix)
		return "index";
		// src/main/resources/templates/index.mustache
	}
}
