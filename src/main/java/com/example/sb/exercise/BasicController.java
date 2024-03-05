package com.example.sb.exercise;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ex")
public class BasicController {
	
	@GetMapping("/hello")	// localhost:8090/sb/ex/hello
	public String hello() {
		return "exercise/hello";		// hello.html
	}
	
	
	
}
