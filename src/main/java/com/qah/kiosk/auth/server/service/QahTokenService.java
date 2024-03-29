package com.qah.kiosk.auth.server.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWTClaimsSet;
import com.qah.kiosk.auth.server.jwt.JWKFacade;
import com.qah.kiosk.auth.server.jwt.JwtClaimsFacade;
import com.qah.kiosk.auth.server.security.KioskUser;
import com.qah.kiosk.auth.server.security.TokenProperties;

@Service
public class QahTokenService {
	
	private TokenProperties properties;
	private JwtClaimsFacade claimsFacade;
	private JWKFacade jwkFacade;
	
	@Autowired
	public void setTokenProperties(TokenProperties properties) {
		this.properties = properties;
	}
	
	@Autowired
	public void setJwtClaimsFacade(JwtClaimsFacade claimsFacade) {
		this.claimsFacade = claimsFacade;
	}
	
	@Autowired
	public void setJWKFacade(JWKFacade jwkFacade) {
		this.jwkFacade = jwkFacade;
	}
	
	public String getBearerHeaderValueFor(Authentication auth) {
		
		KioskUser user = (KioskUser)auth.getPrincipal();
		
		JWTClaimsSet claims = claimsFacade.getClaimsSetFrom(user, properties);
		
		JWSHeader header = null;
		
		try {
			header = jwkFacade.getJWSHeader();
		} catch(Exception ex) {
			throw new RuntimeException("Error creating header");
		}
		
		Payload payload = claimsFacade.getPayloadFromClaims(claims);

		JWSObject jwsObject = jwkFacade.getJWSFromHeaderAndPayload(header, payload);
		
		try {
			jwkFacade.sign(jwsObject);
		} catch(Exception ex) {
			throw new RuntimeException("Error signing the JWS");

		}
		
		return jwsObject.serialize();

	}
	
	public String getJwkSet() {
		String jwkSet = null;
		try {
			jwkSet = this.jwkFacade.getJWK().toPublicJWK().toString();
		} catch(Exception ex) {
			throw new RuntimeException("Error creating JWKSet");
		}
		
		return "{\"keys\":[" + jwkSet + "]}";
	}
	
	
	


}