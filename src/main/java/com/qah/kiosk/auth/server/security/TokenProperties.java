package com.qah.kiosk.auth.server.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/*
 * qah.kiosk.auth.token.expiration.days
 * qah.kiosk.auth.token.expiration.hours
 * qah.kiosk.auth.token.expiration.minutes
 * qah.kiosk.auth.token.expiration.seconds
 * 
 */
@Configuration
@ConfigurationProperties(prefix = "qah.kiosk.auth.token")
public class TokenProperties {
	private Expiration expiration;
	private String issuer;
	
	public void setExpiration(Expiration expiration) {
		this.expiration = expiration;
	}
	
	public Expiration getExpiration() {
		return this.expiration;
	}
	
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	
	public String getIssuer() {
		return this.issuer;
	}
	
	
	
	public static class Expiration {
		int days = 1;
		int hours;
		int minutes;
		int seconds;
		
		public void setDays(int days) {
			this.days = days;
		}
		
		public int getDays() {
			return this.days;
		}
		
		public void setHours(int hours) {
			this.hours = hours;
		}
		
		public int getHours() {
			return this.hours;
		}
		
		public void setMinutes(int minutes) {
			this.minutes = minutes;
		}
		
		public int getMinutes() {
			return this.minutes;
		}
		
		public void setSeconds(int seconds) {
			this.seconds = seconds;
		}
		
		public int getSeconds() {
			return this.seconds;
		}
		
	}
	
}
