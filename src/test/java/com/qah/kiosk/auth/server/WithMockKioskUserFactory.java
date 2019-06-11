package com.qah.kiosk.auth.server;

import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.qah.kiosk.auth.server.security.KioskUser;

public class WithMockKioskUserFactory implements WithSecurityContextFactory<WithKioskUser>{

	@Override
	public SecurityContext createSecurityContext(WithKioskUser annotation) {
		SimpleGrantedAuthority authority1 = new SimpleGrantedAuthority("ROLE_ADMIN");
		SimpleGrantedAuthority authority2 = new SimpleGrantedAuthority("ROLE_USER");
		SimpleGrantedAuthority authority3 = new SimpleGrantedAuthority("ROLE_OTHER");
		
		String username = annotation.username() != null ? annotation.username() : "test-username";
		
		KioskUser testUser = new KioskUser(username,
											"test-password",
											true, true, true, true,
											Arrays.asList(authority1,authority2, authority3),
											"Nathan", "Farciert");
		
		UsernamePasswordAuthenticationToken testAuth = new UsernamePasswordAuthenticationToken(
															testUser, 
															"test-password",
															testUser.getAuthorities());
		
		
		
		
		
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		
		context.setAuthentication(testAuth);
		
		return context;
	}

}
