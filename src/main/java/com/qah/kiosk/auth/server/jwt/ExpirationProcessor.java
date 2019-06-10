package com.qah.kiosk.auth.server.jwt;

import java.util.Calendar;

import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.qah.kiosk.auth.server.security.KioskUser;
import com.qah.kiosk.auth.server.security.TokenProperties;
import com.qah.kiosk.auth.server.security.TokenProperties.Expiration;

public class ExpirationProcessor implements JwtClaimProcessor {

	private TokenProperties properties;
	
	public ExpirationProcessor(TokenProperties properties) {
		this.properties = properties;
	}
	@Override
	public Builder process(Builder builder, KioskUser user) {
		Expiration exp = properties.getExpiration();
		
		Calendar calendar = Calendar.getInstance();
		
		if(exp == null) {
			calendar.roll(Calendar.HOUR, true);
			builder.expirationTime(calendar.getTime());
			return builder;
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
		
		return builder;
	}

}
