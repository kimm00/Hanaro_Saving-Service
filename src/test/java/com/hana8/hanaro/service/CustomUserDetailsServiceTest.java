package com.hana8.hanaro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.hana8.hanaro.common.enums.Role;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

	@Mock
	private MemberRepository repository;

	@InjectMocks
	private CustomUserDetailsService service;

	@Test
	void loadUserByUsername() {
		assertThrows(Exception.class, () -> service.loadUserByUsername("test@test.com"));
	}

	@Test
	void loadUserByUsername_success() {
		// given
		String email = "test@test.com";

		Member member = Member.builder()
			.email(email)
			.password("1234")
			.role(Role.ROLE_USER)
			.build();

		when(repository.findByEmail(email))
			.thenReturn(Optional.of(member));

		// when
		UserDetails result = service.loadUserByUsername(email);

		// then
		assertNotNull(result);
		assertEquals(email, result.getUsername());
		assertEquals("1234", result.getPassword());

		verify(repository).findByEmail(email);
	}

	@Test
	void loadUserByUsername_fail() {
		String email = "test@test.com";

		when(repository.findByEmail(email))
			.thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class,
			() -> service.loadUserByUsername(email));
	}
}
