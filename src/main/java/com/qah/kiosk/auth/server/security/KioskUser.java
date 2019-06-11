package com.qah.kiosk.auth.server.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class KioskUser extends User{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8567954083001898423L;

	private String firstName;
	private String lastName;

	
	public KioskUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public KioskUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
	
	public KioskUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, String firstName, String lastName) {
		this(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.firstName = firstName;
		this.lastName = lastName;

	}
	public String getFirstName() {
		return this.firstName;
	}
	
	
	public String getLastName() {
		return this.lastName;
	}



}
