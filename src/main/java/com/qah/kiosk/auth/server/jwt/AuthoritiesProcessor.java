package com.qah.kiosk.auth.server.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.qah.kiosk.auth.server.security.KioskUser;

public class AuthoritiesProcessor implements JwtClaimProcessor{
	public final static String AUTHORITIES_CLAIM_NAME = "scopes";


	@Override
	public Builder process(Builder builder, KioskUser user) {
		Collection<GrantedAuthority> authorities = user.getAuthorities();
		
		if(authorities == null) {
			return builder;
		}
		
		StringBuilder sb = new StringBuilder();
		
		authorities.stream().forEach(i -> {
			sb.append(i.getAuthority());
			sb.append(" ");
		});
		
		if(sb.length() < 1) {
			return builder;
		}
		
		String scopes = sb.toString().trim();
		
		builder.claim(AUTHORITIES_CLAIM_NAME, scopes);		return null;
	}
	

}
