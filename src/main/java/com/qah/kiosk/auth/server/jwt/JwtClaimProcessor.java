package com.qah.kiosk.auth.server.jwt;

import com.nimbusds.jwt.JWTClaimsSet;
import com.qah.kiosk.auth.server.security.KioskUser;

public interface JwtClaimProcessor {

	JWTClaimsSet.Builder process(JWTClaimsSet.Builder builder, KioskUser user);
	
	default boolean isActive() { return true; }
}
