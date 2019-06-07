package com.qah.kiosk.auth.server.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/**").authenticated()
				.and()
			.httpBasic();
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.inMemoryAuthentication().withUser(new KioskUser(null, null, null));
	}
	
}
