package com.qah.kiosk.auth.server.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.jwk.JWK;
import com.qah.kiosk.auth.server.jwt.JWKFacade;
import com.qah.kiosk.auth.server.jwt.JwtClaimsFacade;
import com.qah.kiosk.auth.server.security.KioskUser;

public class QahTokenServiceTest {
	
	private QahTokenService service;
	
	@Before
	public void setup() {
		service = new QahTokenService();
	}
	
	@Test
	public void verify_getBearerHeaderValueFor_delegatesToJwtClaimsFacade() {
		Authentication mockAuthentication = mock(Authentication.class);
		when(mockAuthentication.getPrincipal()).thenReturn(mock(KioskUser.class));
		
		JwtClaimsFacade mockClaimsFacade = mock(JwtClaimsFacade.class);
		JWKFacade mockJWKFacade = mock(JWKFacade.class);
		doReturn(mock(JWSObject.class))
			.when(mockJWKFacade).getJWSFromHeaderAndPayload(any(), any());
		
		service.setJwtClaimsFacade(mockClaimsFacade);
		service.setJWKFacade(mockJWKFacade);

		
		service.getBearerHeaderValueFor(mockAuthentication);
		verify(mockClaimsFacade).getClaimsSetFrom(any(), any());
		verify(mockClaimsFacade).getPayloadFromClaims(any());
		
	}
	
	@Test
	public void verify_getBearerHeaderValueFor_delegatesToJWKFacade() throws Exception {
		Authentication mockAuthentication = mock(Authentication.class);
		when(mockAuthentication.getPrincipal()).thenReturn(mock(KioskUser.class));
		
		JwtClaimsFacade mockClaimsFacade = mock(JwtClaimsFacade.class);
		JWKFacade mockJWKFacade = mock(JWKFacade.class);
		doReturn(mock(JWSObject.class))
			.when(mockJWKFacade).getJWSFromHeaderAndPayload(any(), any());
		
		service.setJwtClaimsFacade(mockClaimsFacade);
		service.setJWKFacade(mockJWKFacade);

		
		service.getBearerHeaderValueFor(mockAuthentication);
		verify(mockJWKFacade).getJWSFromHeaderAndPayload(any(), any());
		verify(mockJWKFacade).getJWSHeader();
		
	}
	
	@Test
	public void verify_getJwkSet_returnsKeys() throws Exception {
		JWK mockJwk = mock(JWK.class);
		JWK mockPublicJwk = mock(JWK.class);
		
		when(mockPublicJwk.toString()).thenReturn("");
		
		when(mockJwk.toPublicJWK()).thenReturn(mockPublicJwk);
		
		JWKFacade mockJWKFacade = mock(JWKFacade.class);
		when(mockJWKFacade.getJWK()).thenReturn(mockJwk);
		
		service.setJWKFacade(mockJWKFacade);		
		
		Assert.assertTrue(service.getJwkSet().contains("\"keys\":"));
	}

}
