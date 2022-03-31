package com.example.demo.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.DemoService;

@Controller
public class DemoController {
	
	@Autowired
	private DemoService demoService;
}
