package com.qah.kiosk.auth.server.jwt;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;

import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWTClaimsSet;
import com.qah.kiosk.auth.server.security.KioskUser;
import com.qah.kiosk.auth.server.security.TokenProperties;

public class JwtClaimsFacade {
	public final static String AUTHORITIES_CLAIM_NAME = "scopes";
	
	private List<JwtClaimProcessor> processors;
	
	
	public JwtClaimsFacade(JwtClaimProcessor... processors) {
		
		this.processors = new ArrayList<>();
		
		if(processors == null) {
			return;
		}

		Arrays.asList(processors)
			.stream()
			.forEach( i -> this.processors.add((JwtClaimProcessor)i));

	}
	
	public JWTClaimsSet getClaimsSetFrom(KioskUser user, TokenProperties properties) {
		
		JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder();
		
		this.processors.stream()
							.forEach(i -> i.process(claimsSetBuilder, user));
		
		return claimsSetBuilder.build();

	}
	
	public Payload getPayloadFromClaims(JWTClaimsSet claims){
		return new Payload(claims.toJSONObject());
	}
	

}
