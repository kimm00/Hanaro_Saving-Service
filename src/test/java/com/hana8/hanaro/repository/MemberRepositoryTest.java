package com.hana8.hanaro.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hana8.hanaro.entity.Member;

@ExtendWith(MockitoExtension.class)
class MemberRepositoryTest {

	@Mock
	MemberRepository memberRepository;

	@Test
	void findByEmail() {
		when(memberRepository.findByEmail("test@test.com"))
			.thenReturn(Optional.of(new Member()));

		var result = memberRepository.findByEmail("test@test.com");

		assertThat(result).isPresent();
	}

	@Test
	void existsByEmail() {
		when(memberRepository.existsByEmail("test@test.com"))
			.thenReturn(true);

		boolean result = memberRepository.existsByEmail("test@test.com");

		assertThat(result).isTrue();
	}
}
