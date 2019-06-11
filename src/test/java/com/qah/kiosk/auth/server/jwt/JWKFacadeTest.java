package com.qah.kiosk.auth.server.jwt;

import org.junit.Assert;
import org.junit.Test;

public class JWKFacadeTest {

	@Test
	public void verify_derFilesLoaded() throws Exception {
		JWKFacade facade = new JWKFacade();
		Assert.assertNotNull(facade.getJWK());
	}
}
