package com.bolsadeideas.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LocaleController {

	@GetMapping("/locale")
	public String locale(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String context = scheme + "://" + serverName + ":" + serverPort;

		String ultimaUrl = request.getHeader("referer").substring(context.length());

		return "redirect:".concat(ultimaUrl);
	}

}
