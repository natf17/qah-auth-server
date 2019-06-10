package com.qah.kiosk.auth.server.jwt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.qah.kiosk.auth.server.security.KioskUser;
import com.qah.kiosk.auth.server.security.TokenProperties;
import com.qah.kiosk.auth.server.security.TokenProperties.Expiration;

public class ExpirationProcessorTest {
	
	@Test
	public void givenNoExpirationTokenProperties_process_adds1HourExpiration() {
		Builder mockBuilder = mock(JWTClaimsSet.Builder.class);
		KioskUser user = mock(KioskUser.class);
		TokenProperties properties = mock(TokenProperties.class);
		
		ExpirationProcessor processor = new ExpirationProcessor(properties);
		
		processor.process(mockBuilder, user);
		
		ArgumentCaptor<Date> expirationTime = ArgumentCaptor.forClass(Date.class);
		
		verify(mockBuilder).expirationTime(expirationTime.capture());
		
		Calendar received = Calendar.getInstance();
		received.setTime(expirationTime.getValue());
		
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, 1);

		Assert.assertTrue(received.get(Calendar.YEAR) == now.get(Calendar.YEAR));
		Assert.assertTrue(received.get(Calendar.DATE) == now.get(Calendar.DATE));
		Assert.assertTrue(received.get(Calendar.HOUR) == now.get(Calendar.HOUR));
		
	}
	
	@Test
	public void givenExpirationTokenProperties_process_addsCorrectExpiration() {
		Builder mockBuilder = mock(JWTClaimsSet.Builder.class);
		KioskUser user = mock(KioskUser.class);
		TokenProperties properties = mock(TokenProperties.class);
		
		Expiration exp = new Expiration();
		exp.setDays(0);
		exp.setHours(1);
		exp.setMinutes(5);
		exp.setSeconds(10);
		
		when(properties.getExpiration()).thenReturn(exp);
		
		ExpirationProcessor processor = new ExpirationProcessor(properties);
		
		processor.process(mockBuilder, user);
		
		ArgumentCaptor<Date> expirationTime = ArgumentCaptor.forClass(Date.class);
		
		verify(mockBuilder).expirationTime(expirationTime.capture());
		
		Calendar received = Calendar.getInstance();
		received.setTime(expirationTime.getValue());
		
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, 1);
		now.add(Calendar.MINUTE, 5);


		Assert.assertTrue(received.get(Calendar.YEAR) == now.get(Calendar.YEAR));
		Assert.assertTrue(received.get(Calendar.DATE) == now.get(Calendar.DATE));
		Assert.assertTrue(received.get(Calendar.HOUR) == now.get(Calendar.HOUR));
		Assert.assertTrue(received.get(Calendar.MINUTE) == now.get(Calendar.MINUTE));
		
	}
	
}
