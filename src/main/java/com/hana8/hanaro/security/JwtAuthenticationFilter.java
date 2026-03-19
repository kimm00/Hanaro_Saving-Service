package com.hana8.hanaro.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String[] EXCLUDE_PATTERNS = {
		"/posts/**",
		"/api/public/**",
		"/api/auth/**",
		"/favicon.ico",
		"/actuator/**",
		"/*.html",
		"/swagger-ui/**",
		"/hana8/api-docs/**",
		"/broadcast/**",
	};
	private final JwtUtil jwtUtil;

	private final AntPathMatcher pathMatcher = new AntPathMatcher();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		System.out.println("** path = " + path);

		if (path.startsWith("/api/auth")) {
			return true;
		}

		return Arrays.stream(EXCLUDE_PATTERNS)
			.anyMatch(pattern -> pathMatcher.match(pattern, path));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		System.out.println("=== JwtAuthenticationFilter 실행 ===");
		System.out.println("Request URI = " + request.getRequestURI());
		System.out.println("Authorization = " + request.getHeader("Authorization"));
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			Map<String, Object> claims = jwtUtil.validateToken(authHeader.substring(7));

			String email = (String)claims.get("email");
			String role = (String)claims.get("role");
			System.out.println("role = " + role);

			User user = new User(
				email,
				"",
				List.of(new SimpleGrantedAuthority(role))
			);

			SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
			);

		} catch (Exception e) {
			sendError(response, "ERROR_ACCESS_TOKEN");
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void sendError(HttpServletResponse response, String message) throws IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(objectMapper.writeValueAsString(Map.of("error", message)));
		out.close();
	}

}
