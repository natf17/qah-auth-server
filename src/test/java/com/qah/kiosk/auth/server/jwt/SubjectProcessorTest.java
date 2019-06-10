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

public class SubjectProcessorTest {
	@Test
	public void process_addsCorrectSubject() {
		Builder mockBuilder = mock(JWTClaimsSet.Builder.class);
		KioskUser user = mock(KioskUser.class);
		
		String username = "ralph";
		when(user.getUsername()).thenReturn(username);
		
		SubjectProcessor processor = new SubjectProcessor();
		
		processor.process(mockBuilder, user);
		
		ArgumentCaptor<String> subject = ArgumentCaptor.forClass(String.class);
		
		verify(mockBuilder).subject(subject.capture());
		
		Assert.assertEquals(username, subject.getValue());
		
	}
	

}
