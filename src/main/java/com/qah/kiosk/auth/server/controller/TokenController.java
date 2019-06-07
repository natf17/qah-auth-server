package com.qah.kiosk.auth.server.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qah.kiosk.auth.server.service.QahTokenService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class TokenController {
	private QahTokenService tokenService;
	
	@Autowired
	public void setTokenService(QahTokenService tokenService) {
		this.tokenService = tokenService;
	}

	@RequestMapping(path = "/", method = GET)
	public ResponseEntity<String> getToken(Authentication auth) {
		String token = tokenService.getBearerHeaderValueFor(auth);
		
		HttpHeaders headers = new HttpHeaders(); 
		headers.add(HttpHeaders.AUTHORIZATION, token.startsWith("Bearer") ? token : "Bearer " + token);
		
		
		return new ResponseEntity<>(headers, HttpStatus.ACCEPTED);
	}
}
