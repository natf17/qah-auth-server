package com.qah.kiosk.auth.server.jwt;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

@Component
public class JWKFacade {
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public void sign(JWSObject jwsObject) throws Exception {
		JWSSigner signer = new RSASSASigner((RSAPrivateKey)getPrivateKey());

		jwsObject.sign(signer);
	}
	
	public JWSHeader getJWSHeader() throws Exception {
		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
				.keyID(getJWK().getKeyID())
				.build();
		return header;
	}
	
	public JWSObject getJWSFromHeaderAndPayload(JWSHeader header, Payload payload) {
		return new JWSObject(header, payload);
	}
	
	
	public JWK getJWK() throws Exception {
		PrivateKey priv = getPrivateKey();
		PublicKey pub = getPublicKey();
		
		JWK jwk = new RSAKey.Builder((RSAPublicKey)pub)
			    .privateKey((RSAPrivateKey)priv)
			    .keyUse(KeyUse.SIGNATURE)
			    .keyID(UUID.randomUUID().toString())
			    .build();
		
		return jwk;
	}
	
	private PrivateKey getPrivateKey() throws Exception {
		if(privateKey == null) {

			File file = ResourceUtils.getFile("classpath:crypto/private_key.der");
			
			byte[] keyBytes = Files.readAllBytes(file.toPath());
	
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			
			privateKey = kf.generatePrivate(spec);
		}
		return privateKey;
		
	}
	
	private PublicKey getPublicKey() throws Exception {
		if(publicKey == null) {
			
			File file = ResourceUtils.getFile("classpath:crypto/public_key.der");
			
			byte[] keyBytes = Files.readAllBytes(file.toPath());
	
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			publicKey = kf.generatePublic(spec);
		}
		
		return publicKey;
		
	}

}
