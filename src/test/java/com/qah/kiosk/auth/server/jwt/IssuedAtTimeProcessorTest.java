package com.qah.kiosk.auth.server.jwt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.qah.kiosk.auth.server.security.KioskUser;


public class IssuedAtTimeProcessorTest {
	
	@Test
	public void process_addsCorrectIssueTime() {
		Builder mockBuilder = mock(JWTClaimsSet.Builder.class);
		KioskUser user = mock(KioskUser.class);
		
		IssuedAtTimeProcessor processor = new IssuedAtTimeProcessor();
		
		processor.process(mockBuilder, user);
		
		ArgumentCaptor<Date> issueTime = ArgumentCaptor.forClass(Date.class);
		
		verify(mockBuilder).issueTime(issueTime.capture());
		
		Calendar received = Calendar.getInstance();
		received.setTime(issueTime.getValue());
		
		Calendar now = Calendar.getInstance();

		Assert.assertTrue(received.get(Calendar.YEAR) == now.get(Calendar.YEAR));
		Assert.assertTrue(received.get(Calendar.DATE) == now.get(Calendar.DATE));
		Assert.assertTrue(received.get(Calendar.HOUR) == now.get(Calendar.HOUR));
		Assert.assertTrue(received.get(Calendar.MINUTE) == now.get(Calendar.MINUTE));
		
	}

}
