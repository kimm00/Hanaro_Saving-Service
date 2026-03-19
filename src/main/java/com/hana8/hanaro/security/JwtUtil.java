package com.hana8.hanaro.security;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.hana8.hanaro.security.exception.CustomJwtException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private final SecretKey secretKey;

	public JwtUtil(@Value("${jwt.secret}") String JwtSecret) {
		secretKey = Keys.hmacShaKeyFor(JwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	public Map<String, Object> validateToken(String token) {
		try {
			return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		} catch (ExpiredJwtException e) {
			throw new CustomJwtException("Token Expired");
		} catch (JwtException e) {
			throw new CustomJwtException("Jwt Error");
		} catch (Exception e) {
			throw new CustomJwtException("Error: " + e.getMessage());
		}
	}

	public Map<String, Object> authenticationToClaims(Authentication authentication) {

		Object principal = authentication.getPrincipal();

		if (!(principal instanceof org.springframework.security.core.userdetails.User user)) {
			throw new CustomJwtException("Invalid Authentication");
		}

		Map<String, Object> claims = Map.of(
			"email", user.getUsername(),
			"role", user.getAuthorities().iterator().next().getAuthority()
		);

		return Map.of(
			"accessToken", generateToken(claims, 60),
			"refreshToken", generateToken(claims, 60 * 24)
		);
	}

	public String generateToken(Map<String, Object> valueMap, int min) {
		return Jwts.builder()
			.header().add("typ", "JWT").and()
			.claims().add(valueMap).and()
			.issuedAt(Date.from(ZonedDateTime.now().toInstant()))
			.expiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
			.signWith(secretKey)
			.compact();
	}

}
