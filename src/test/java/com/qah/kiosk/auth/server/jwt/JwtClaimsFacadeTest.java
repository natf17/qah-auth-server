package com.qah.kiosk.auth.server.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.qah.kiosk.auth.server.security.KioskUser;
import com.qah.kiosk.auth.server.security.TokenProperties;

public class JwtClaimsFacadeTest {

	
	@Test
	public void verify_getClaimsSetFrom_delegatesToEachProcessor() {
		
		JwtClaimProcessor processor1 = mock(JwtClaimProcessor.class);
		JwtClaimProcessor processor2 = mock(JwtClaimProcessor.class);
		JwtClaimProcessor processor3 = mock(JwtClaimProcessor.class);
		
		
		JwtClaimsFacade service = new JwtClaimsFacade(processor1, processor2, processor3);
		
				
		service.getClaimsSetFrom(mock(KioskUser.class), mock(TokenProperties.class));
		
		
		
		verify(processor1).process(any(), any());
		verify(processor2).process(any(), any());
		verify(processor3).process(any(), any());

		
	}
}
