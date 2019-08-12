package com.qah.kiosk.auth.server.security;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Key;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyConverter;
import com.nimbusds.jose.jwk.KeyType;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.proc.JWSVerifierFactory;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test-application.properties")
@AutoConfigureMockMvc
public class BasicAuthenticationTests {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private MockMvc mockMvc;
	
	private String username = "sampleUsername";
	private String password = "samplePassword";
	
	@Before
	public void setup() {
		
		int enabled = 1;
		String firstName = "Nathan";
		String lastName = "Farciert";
		
		String authority1 = "ADMIN";
		String authority2 = "USER";
		
		jdbc.update("DELETE FROM users");
		jdbc.update("DELETE FROM authorities");
		
		// save a user
		jdbc.update("INSERT INTO users(username, password, enabled, firstName, lastName) VALUES(?,?,?,?,?)"
						, username, encoder.encode(this.password), enabled, firstName, lastName);
		
		// save authorities for user
		jdbc.update("INSERT INTO authorities(username, authority) VALUES(?,?)"
				, username, authority1);
		jdbc.update("INSERT INTO authorities(username, authority) VALUES(?,?)"
				, username, authority2);

		
	}
	
	@Test
	public void givenInvalidCredentials_return401() throws Exception {
		String base64EncodedUsernamePassword = "";
		mockMvc.perform(post("/token")
							.accept(MediaType.APPLICATION_JSON)
							.header(HttpHeaders.AUTHORIZATION, "BASIC " + base64EncodedUsernamePassword))
				.andExpect(status().isUnauthorized()); // via HttpStatusEntryPoint		
	}
	
	@Test
	public void givenValidCredentials_return202() throws Exception {
		String base64EncodedUsernamePassword = Base64.encode(this.username + ":" + this.password).toString();
		
		mockMvc.perform(post("/token")
							.contentType(MediaType.APPLICATION_JSON)
							.header(HttpHeaders.AUTHORIZATION, "BASIC " + base64EncodedUsernamePassword))
				.andExpect(status().isAccepted());		
	}
	
	@Test
	public void givenValidCredentials_returnValidUser() throws Exception {
		String base64EncodedUsernamePassword = Base64.encode(this.username + ":" + this.password).toString();
		
		// obtain JWKSet
		MvcResult jwkResult = mockMvc.perform(get("/.well-known/jwks.json")
							.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		String response = jwkResult.getResponse().getContentAsString();
		
		JWKSet jwkSet = JWKSet.parse(response);
		System.out.println(jwkSet.toString());
		
		// obtain token
		MvcResult tokenResult = mockMvc.perform(post("/token")
							.contentType(MediaType.APPLICATION_JSON)
							.header(HttpHeaders.AUTHORIZATION, "BASIC " + base64EncodedUsernamePassword)
							.header("X-Requested-With", "XMLHttpRequest"))
				.andExpect(status().isAccepted())
				.andExpect(content().string(containsString("{\"token\":\"")))
				.andReturn();
		String tokenString = tokenResult.getResponse().getContentAsString();
		
		Assert.assertTrue(tokenString.startsWith("{\"token\":\""));
		
		// extract the token value
		String tokenValue = tokenString.substring(10, tokenString.length() - 2);
		System.out.println(tokenValue);
		
		// see NimbusJwtDecoderJwkSupport.decode()
		// see NimbusJwtDecoderJwkSupport.parse()
		JWT jwt = JWTParser.parse(tokenValue);
		// end NimbusJwtDecoderJwkSupport.parse()
		
		
		// see NimbusJwtDecoderJwkSupport.createJwt()
		// see DefaultJWTProcessor.process()
		// see JWSVerificationKeySelector.selectJWSKeys
		SignedJWT signedJwt = (SignedJWT)jwt;
		JWSHeader jwsHeader = signedJwt.getHeader();
		// see JWSVerificationKeySelector.createJWKMatcher
		JWKMatcher jwkMatcher = new JWKMatcher.Builder()
										.keyType(KeyType.forAlgorithm(JWSAlgorithm.RS256))
										.keyID(jwsHeader.getKeyID())
										.keyUses(KeyUse.SIGNATURE, null)
										.algorithms(JWSAlgorithm.RS256, null)
										.build();
		// end JWSVerificationKeySelector.createJWKMatcher
		JWKSelector jwkSelector = new JWKSelector(jwkMatcher);
		
		// see RemoteJWKSet.get
		List<JWK> matches = jwkSelector.select(jwkSet);
		Assert.assertTrue(matches.size() != 0);

		// end RemoteJWKSet.get
		
		
		List<Key> sanitizedKeyList = new LinkedList<>();

		for (Key key: KeyConverter.toJavaKeys(matches)) {
			if (key instanceof PublicKey) {
				sanitizedKeyList.add(key);
			} // skip asymmetric private keys
		}
		
		// end JWSVerificationKeySelector.selectJWSKeys
		
		// still in DefaultJWTProcessor.process ...
		Assert.assertTrue(sanitizedKeyList.size() != 0);
		
		ListIterator<? extends Key> it = sanitizedKeyList.listIterator();
		
		JWSVerifierFactory verifierFactory = new DefaultJWSVerifierFactory();
		JWTClaimsSet verifiedClaimsSet = null;
		while (it.hasNext()) {
			JWSVerifier verifier = verifierFactory.createJWSVerifier(signedJwt.getHeader(), it.next());
			if (verifier == null) {
				continue;
			}
			
			final boolean validSignature = signedJwt.verify(verifier);

			if (validSignature) {
				// see DefaultJWTProcessor.verifyAndReturnClaims()
				// this id disabled by Spring Boot, do we just return the
				// claim set
				verifiedClaimsSet = signedJwt.getJWTClaimsSet();
				break;
				
			}
		}
		
		// make sure the payload/JWTClaimsSet has been verified
		Assert.assertNotNull(verifiedClaimsSet);
		
		// end DefaultJWTProcessor.process

		// back in NimbusJwtDecoderJwkSupport.createJwt()
		// verify it hasn't expired
		
		Map<String, Object> headers = new LinkedHashMap<>(jwt.getHeader().toJSONObject());
		Converter<Map<String, Object>, Map<String, Object>> claimSetConverter =
				MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());
		Map<String, Object> claims = claimSetConverter.convert(verifiedClaimsSet.getClaims());

		Instant expiresAt = (Instant) claims.get(JwtClaimNames.EXP);
		Instant issuedAt = (Instant) claims.get(JwtClaimNames.IAT);
		Jwt jwtFinalSpring = new Jwt(tokenValue, issuedAt, expiresAt, headers, claims);
		
		// end NimbusJwtDecoderJwkSupport.createJwt
		
		// back in NimbusJwtDecoderJwkSupport.decode
		// see NimbusJwtDecoderJwkSupport.validateJwt
		OAuth2TokenValidator<Jwt> jwtValidator = JwtValidators.createDefault();
		OAuth2TokenValidatorResult result = jwtValidator.validate(jwtFinalSpring);
		
		Assert.assertFalse(result.hasErrors());
		
		
		// check the original information in the token
		Assert.assertTrue(jwtFinalSpring.getSubject().equals(username));
		String authorities = (String)(jwtFinalSpring.getClaims().get("scope"));
		Assert.assertTrue(authorities.split(" ").length == 2);
		
		
		
	}

}
