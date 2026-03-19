package com.hana8.hanaro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;

class ControllerExceptionHandlerTest {

	private ControllerExceptionHandler handler;

	@BeforeEach
	void setUp() {
		handler = new ControllerExceptionHandler();
	}

	@Test
	void handleIllegalExceptionHandler() {
		IllegalArgumentException e = new IllegalArgumentException("잘못된 요청");

		ResponseEntity<String> result = handler.handleIllegalExceptionHandler(e);

		assertEquals(400, result.getStatusCode().value());
		assertTrue(result.getBody().contains("Warn"));
	}

	@Test
	void handleViolationExceptionHandler() {
		ConstraintViolation<?> violation = mock(ConstraintViolation.class);
		Path path = mock(Path.class);

		when(path.toString()).thenReturn("field");
		when(violation.getPropertyPath()).thenReturn(path);
		when(violation.getMessage()).thenReturn("violation");

		ConstraintViolationException e =
			new ConstraintViolationException(Set.of(violation));

		ResponseEntity<Map<String, String>> result =
			handler.handleViolationExceptionHandler(e);

		assertEquals(400, result.getStatusCode().value());
		assertTrue(result.getBody().containsKey("field"));
	}

	@Test
	void handleNotFoundException() throws Exception {
		NoHandlerFoundException e =
			new NoHandlerFoundException("GET", "/test", null);

		ResponseEntity<String> result = handler.handleNotFoundException(e);

		assertEquals(404, result.getStatusCode().value());
	}

	@Test
	void handleOthersExceptionHandler() {
		Exception e = new Exception("서버 에러");

		ResponseEntity<String> result = handler.handleOthersExceptionHandler(e);

		assertEquals(500, result.getStatusCode().value());
		assertTrue(result.getBody().contains("Error"));
	}
}
