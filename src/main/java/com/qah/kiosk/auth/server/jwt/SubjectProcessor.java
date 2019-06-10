package com.qah.kiosk.auth.server.jwt;

import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.qah.kiosk.auth.server.security.KioskUser;

public class SubjectProcessor implements JwtClaimProcessor {

	@Override
	public Builder process(Builder builder, KioskUser user) {
		return builder.subject(user.getUsername());
	}

}
