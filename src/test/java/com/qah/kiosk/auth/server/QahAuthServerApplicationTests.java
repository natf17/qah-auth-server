package com.qah.kiosk.auth.server;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test-application.properties")
@AutoConfigureMockMvc
public class QahAuthServerApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithKioskUser
	public void getToken() throws Exception {
		mockMvc.perform(post("/token")
							.accept(MediaType.APPLICATION_JSON)
							.with(csrf()))
					.andExpect(status().isAccepted())
					.andExpect(content().string(containsString("token")));
		
	}
	
	@Test
	public void getJWKSet() throws Exception {
		mockMvc.perform(get("/.well-known/jwks.json")
				.accept(MediaType.APPLICATION_JSON)
				.with(csrf()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.keys.size()").value(1));
	}
	
	

}
