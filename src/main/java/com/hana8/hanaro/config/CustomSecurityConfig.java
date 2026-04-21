package com.hana8.hanaro.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hana8.hanaro.security.JwtAuthenticationFilter;
import com.hana8.hanaro.security.handler.CustomAccessDeniedHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) {
		log.info(" --- securityConfig");
		System.out.println("** SecurityConfig.filterChain");
		http
			// .httpBasic(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.cors(config -> config.configurationSource(corsConfigurationSource()))
			.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/api/auth/**",            // 로그인
					"/api/members/register",   // 회원가입
					"/swagger-ui/**",
					"/v3/api-docs/**",
					"/api-docs/**",
					"/swagger-ui.html",
					"/actuator/**"
				).permitAll()
				.requestMatchers("/api/products/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.PATCH, "/api/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.GET, "/api/members/**").hasRole("ADMIN")
				.requestMatchers("/api/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			)

			.exceptionHandling(config -> config.accessDeniedHandler(accessDeniedHandler))
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	private CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOriginPatterns(List.of("*"));
		config.setAllowedMethods(List.of(
			HttpMethod.GET.name(),
			HttpMethod.POST.name(),
			HttpMethod.PATCH.name(),
			HttpMethod.OPTIONS.name(),
			HttpMethod.DELETE.name()));
		config.setAllowedHeaders(List.of(
			HttpHeaders.AUTHORIZATION,
			HttpHeaders.CACHE_CONTROL,
			HttpHeaders.CONTENT_TYPE));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
