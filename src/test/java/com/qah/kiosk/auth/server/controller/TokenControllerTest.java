package com.qah.kiosk.auth.server.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.qah.kiosk.auth.server.service.QahTokenService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = TokenController.class)
public class TokenControllerTest {
	private static final String token = "aaa111222333444555";
	
	@MockBean
	private QahTokenService tokenService;
	
	@MockBean
	JdbcTemplate jdbc;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@Before
	public void setup() {
		doReturn(token).when(tokenService).getBearerHeaderValueFor(any());
	}
		
	@Test
	@WithMockUser
	public void whenAuthenticated_tokenEndpoint_returnsToken() throws Exception {
			mockMvc.perform(post("/token").with(csrf()))
						.andExpect(status().isAccepted())
						.andExpect(content().string("{\"token\":\"" + token + "\"}"));
	}
	
}
