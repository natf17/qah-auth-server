package com.qah.kiosk.auth.server.jwt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.qah.kiosk.auth.server.security.KioskUser;
import com.qah.kiosk.auth.server.security.TokenProperties;

public class IssuerProcessorTest {

	@Test
	public void givenNoIssuerTokenProperties_process_addsDefaultIssuer() {
		Builder mockBuilder = mock(JWTClaimsSet.Builder.class);
		KioskUser user = mock(KioskUser.class);
		TokenProperties properties = mock(TokenProperties.class);
		
		IssuerProcessor processor = new IssuerProcessor(properties);
		
		processor.process(mockBuilder, user);
		
		ArgumentCaptor<String> issuer = ArgumentCaptor.forClass(String.class);
		
		verify(mockBuilder).issuer(issuer.capture());
		
		Assert.assertEquals("QAH KIOSK AUTH SERVER", issuer.getValue());
		
	}
	
	@Test
	public void givenIssuerTokenProperties_process_addsCorrectIssuer() {
		Builder mockBuilder = mock(JWTClaimsSet.Builder.class);
		KioskUser user = mock(KioskUser.class);
		TokenProperties properties = mock(TokenProperties.class);
		
		String newIssuer = "default-issuer";
		
		when(properties.getIssuer()).thenReturn(newIssuer);
		
		IssuerProcessor processor = new IssuerProcessor(properties);
		
		processor.process(mockBuilder, user);
		
		ArgumentCaptor<String> issuer = ArgumentCaptor.forClass(String.class);
		
		verify(mockBuilder).issuer(issuer.capture());
		
		Assert.assertEquals(newIssuer, issuer.getValue());
		
	}
	
}
