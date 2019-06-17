package com.qah.kiosk.auth.server.jwt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.qah.kiosk.auth.server.security.KioskUser;

public class AuthoritiesProcessorTest {

	
	@Test
	public void given3Authorities_process_addsClaim() {
		Builder mockBuilder = mock(JWTClaimsSet.Builder.class);
		KioskUser user = mock(KioskUser.class);
		
		SimpleGrantedAuthority authority1 = new SimpleGrantedAuthority("ROLE_ADMIN");
		SimpleGrantedAuthority authority2 = new SimpleGrantedAuthority("ROLE_USER");
		SimpleGrantedAuthority authority3 = new SimpleGrantedAuthority("ROLE_OTHER");
		
		when(user.getAuthorities()).thenReturn(Arrays.asList(authority1, authority2, authority3));
		
		AuthoritiesProcessor processor = new AuthoritiesProcessor();
		processor.process(mockBuilder, user);

		ArgumentCaptor<String> claimKey = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> claimValue = ArgumentCaptor.forClass(String.class);
		
		verify(mockBuilder).claim(claimKey.capture(), claimValue.capture());
				
		Assert.assertEquals("scope", claimKey.getValue());
		Assert.assertEquals("ROLE_ADMIN ROLE_USER ROLE_OTHER", claimValue.getValue());
		
		
	}
}
