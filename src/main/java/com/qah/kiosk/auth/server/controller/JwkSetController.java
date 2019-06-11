package com.qah.kiosk.auth.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qah.kiosk.auth.server.service.QahTokenService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class JwkSetController {
	
	private QahTokenService tokenService;
	
	@Autowired
	public void setTokenService(QahTokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	@RequestMapping(path="/.well-known/jwks.json", method = GET, produces="application/json")
	public ResponseEntity<String> getToken(Authentication auth) {
		
		String jwkSet = tokenService.getJwkSet();

		return new ResponseEntity<>(jwkSet, HttpStatus.OK);
	}
}
