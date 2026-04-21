package com.hana8.hanaro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import com.hana8.hanaro.dto.LoginRequest;
import com.hana8.hanaro.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private AuthController authController;

	@Test
	void signIn_success() {
		LoginRequest req = new LoginRequest("admin@email.com", "12345678");

		Authentication auth = mock(Authentication.class);

		when(authenticationManager.authenticate(any()))
			.thenReturn(auth);

		when(jwtUtil.authenticationToClaims(auth))
			.thenReturn(Map.of("token", "TOKEN"));

		ResponseEntity<?> result = authController.signIn(req);

		assertEquals(200, result.getStatusCode().value());
		verify(authenticationManager).authenticate(any());
	}
}
