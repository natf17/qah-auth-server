package com.qah.kiosk.auth.server.jwt;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWTClaimsSet;
import com.qah.kiosk.auth.server.security.KioskUser;
import com.qah.kiosk.auth.server.security.TokenProperties;
import com.qah.kiosk.auth.server.security.TokenProperties.Expiration;

@Component
public class JwtClaimsFacade {
	public final static String DEFAULT_ISSUER = "QAH KIOSK AUTH SERVER"; 
	public final static String AUTHORITIES_CLAIM_NAME = "scopes";
	
	public JWTClaimsSet getClaimsSetFrom(KioskUser user, TokenProperties properties) {
		
		JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder();
		
		processSubject(claimsSetBuilder, user);
		processExpiration(claimsSetBuilder, properties);
		processIssuer(claimsSetBuilder, properties);
		processIssuedAtTime(claimsSetBuilder);
		processAuthorities(claimsSetBuilder, user);
		
		return claimsSetBuilder.build();
	}
	
	public Payload getPayloadFromClaims(JWTClaimsSet claims){
		return new Payload(claims.toJSONObject());
	}
	
	protected void processSubject(JWTClaimsSet.Builder builder, KioskUser user) {
		builder.subject(user.getUsername());
	}
	
	protected void processExpiration(JWTClaimsSet.Builder builder, TokenProperties properties) {
		Expiration exp = properties.getExpiration();
		
		Calendar calendar = Calendar.getInstance();
		//tomorrow.roll(Calendar.DATE, true);
		
		if(exp == null) {
			calendar.roll(Calendar.HOUR, true);
			builder.expirationTime(calendar.getTime());
			return;
		}
		
		int days = exp.getDays();
		int hours = exp.getHours();
		int minutes = exp.getMinutes();
		int seconds = exp.getSeconds();
		
		if(days != 0) {
			calendar.add(Calendar.DATE, days);
		}
		
		if(hours != 0) {
			calendar.add(Calendar.HOUR, hours);
		}
		
		if(minutes != 0) {
			calendar.add(Calendar.MINUTE, minutes);
		}
		
		if(seconds != 0) {
			calendar.add(Calendar.SECOND, seconds);
		}
		
		builder.expirationTime(calendar.getTime());
		
	}
	
	protected void processIssuer(JWTClaimsSet.Builder builder, TokenProperties properties) {
		String issuer = properties.getIssuer();
		if(issuer != null && !issuer.isEmpty()) {
			builder.issuer(issuer);
		} else {
			builder.issuer(DEFAULT_ISSUER);
		}
		
	}
	
	protected void processIssuedAtTime(JWTClaimsSet.Builder builder) {
		builder.issueTime(Calendar.getInstance().getTime());
	}
	
	protected void processAuthorities(JWTClaimsSet.Builder builder, KioskUser user) {
		Collection<GrantedAuthority> authorities = user.getAuthorities();
		
		if(authorities == null) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		
		authorities.stream().forEach(i -> {
			sb.append(i.getAuthority());
			sb.append(" ");
		});
		
		if(sb.length() < 1) {
			return;
		}
		
		String scopes = sb.toString().trim();
		
		builder.claim(AUTHORITIES_CLAIM_NAME, scopes);
		
	}

}
