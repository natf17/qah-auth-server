package com.qah.kiosk.auth.server;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;


@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockKioskUserFactory.class)
public @interface WithKioskUser {
	String username() default "test";	

}
