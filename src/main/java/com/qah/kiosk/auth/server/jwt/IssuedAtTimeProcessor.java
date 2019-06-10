package com.qah.kiosk.auth.server.jwt;

import java.util.Calendar;

import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.qah.kiosk.auth.server.security.KioskUser;

public class IssuedAtTimeProcessor implements JwtClaimProcessor {

	@Override
	public Builder process(Builder builder, KioskUser user) {
		builder.issueTime(Calendar.getInstance().getTime());
		
		return builder;
	}

}
