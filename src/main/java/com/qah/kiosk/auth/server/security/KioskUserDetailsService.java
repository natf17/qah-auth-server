package com.qah.kiosk.auth.server.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

public class KioskUserDetailsService extends JdbcDaoImpl {
	
	public final String DEF_USERS_BY_USERNAME_QUERY_CUSTOM = "select username,password,firstName,lastName,enabled "
			+ "from users " + "where username = ?";
	
	@Override
	protected List<UserDetails> loadUsersByUsername(String username) {
		return getJdbcTemplate().query(this.DEF_USERS_BY_USERNAME_QUERY_CUSTOM,
				new String[] { username }, new RowMapper<UserDetails>() {
					@Override
					public UserDetails mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						String username = rs.getString(1);
						String password = rs.getString(2);
						String firstName = rs.getString(3);
						String lastName = rs.getString(4);
						boolean enabled = rs.getBoolean(5);
						return new KioskUser(username, password, enabled, true, true, true,
								AuthorityUtils.NO_AUTHORITIES, firstName, lastName);
					}

				});
	}
	
	
	@Override
	protected UserDetails createUserDetails(String username, 
			UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
		
		return new KioskUser(username, userFromUserQuery.getPassword(),
										userFromUserQuery.isEnabled(), userFromUserQuery.isAccountNonExpired(),
										userFromUserQuery.isCredentialsNonExpired(), userFromUserQuery.isAccountNonLocked(),
										combinedAuthorities, ((KioskUser)userFromUserQuery).getFirstName(),
										((KioskUser)userFromUserQuery).getLastName());
	}
	
}
