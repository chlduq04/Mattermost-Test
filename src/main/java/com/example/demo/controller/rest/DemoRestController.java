package com.example.demo.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DemoService;

@RestController
@RequestMapping("/rest")
public class DemoRestController {
	@Autowired
	private DemoService demoService;
	
	@GetMapping("/login")
	public String test() {
		demoService.login();
		return "login";
	}
	
	@GetMapping("/send/dm")
	public String sendDirectMessage() {
		demoService.sendDirectMessage();
		return "sendDM";
	}

	@GetMapping("/send/gm")
	public String sendGroupMessage() {
		demoService.sendGroupMessage();
		return "sendGM";
	}
	
	@GetMapping("/get/ch")
	public String getChannelsInTeam() {
		demoService.getChannelListInTeam();
		return "getChannels";
	}
	

}
