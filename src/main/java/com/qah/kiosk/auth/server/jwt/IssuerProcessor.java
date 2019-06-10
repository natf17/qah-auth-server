package com.qah.kiosk.auth.server.jwt;

import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.qah.kiosk.auth.server.security.KioskUser;
import com.qah.kiosk.auth.server.security.TokenProperties;

public class IssuerProcessor implements JwtClaimProcessor {
	public final static String DEFAULT_ISSUER = "QAH KIOSK AUTH SERVER"; 
	
	private TokenProperties properties;

	public IssuerProcessor(TokenProperties properties) {
		this.properties = properties;
	}

	@Override
	public Builder process(Builder builder, KioskUser user) {
		String issuer = properties.getIssuer();
		if(issuer != null && !issuer.isEmpty()) {
			builder.issuer(issuer);
		} else {
			builder.issuer(DEFAULT_ISSUER);
		}
		
		
		return builder;
	}

}
