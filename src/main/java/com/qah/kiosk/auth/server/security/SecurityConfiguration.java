package com.qah.kiosk.auth.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/.well-known/jwks.json").permitAll()
				.antMatchers("/**").authenticated()
				.and()
			.httpBasic()
				.and()
			.csrf()
				.ignoringAntMatchers("/token"); // this POST triggers no side effects
		
		http
        .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(getUserDetailsService())
			   .passwordEncoder(passwordEncoder());
		
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	private UserDetailsService getUserDetailsService() {
		KioskUserDetailsService userDetailsService = new KioskUserDetailsService();
		userDetailsService.setJdbcTemplate(jdbc);
		
		return userDetailsService;
	}
	
	
}
