package com.qah.kiosk.auth.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qah.kiosk.auth.server.jwt.AuthoritiesProcessor;
import com.qah.kiosk.auth.server.jwt.ExpirationProcessor;
import com.qah.kiosk.auth.server.jwt.IssuedAtTimeProcessor;
import com.qah.kiosk.auth.server.jwt.IssuerProcessor;
import com.qah.kiosk.auth.server.jwt.JwtClaimsFacade;
import com.qah.kiosk.auth.server.jwt.SubjectProcessor;
import com.qah.kiosk.auth.server.security.TokenProperties;

@Configuration
public class AppConfiguration {

	@Autowired
	TokenProperties properties;
	
	@Bean
	JwtClaimsFacade jwkFacade(TokenProperties properties) {
		JwtClaimsFacade facade = new JwtClaimsFacade(
									new SubjectProcessor(),
									new ExpirationProcessor(properties),
									new IssuerProcessor(properties),
									new IssuedAtTimeProcessor(),
									new AuthoritiesProcessor()
								);
		
		return facade;
		
	
	}
}
