package com.qah.kiosk.auth.server.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.qah.kiosk.auth.server.service.QahTokenService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = JwkSetController.class)
public class JwkSetControllerTest {
	
	
	@MockBean
	private QahTokenService tokenService;
	
	@MockBean
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MockMvc mockMvc;
	

		
	@Test
	public void whenAuthenticated_tokenEndpoint_returnsToken() throws Exception {
		when(tokenService.getJwkSet()).thenReturn("{\"keys\":[{sample: s1}]}");

		mockMvc.perform(get("/.well-known/jwks.json").accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.keys.size()").value(1));
								
	}
	

}
