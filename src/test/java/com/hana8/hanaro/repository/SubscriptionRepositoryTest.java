package com.hana8.hanaro.repository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hana8.hanaro.entity.Subscription;

@ExtendWith(MockitoExtension.class)
class SubscriptionRepositoryTest {

	@Mock
	SubscriptionRepository repository;

	@Test
	void findByAccountId() {
		when(repository.findByAccountId(1L))
			.thenReturn(Optional.of(new Subscription()));

		var result = repository.findByAccountId(1L);

		assertThat(result).isPresent();
	}

	@Test
	void findByMemberId() {
		when(repository.findByMemberId(1L))
			.thenReturn(List.of(new Subscription()));

		var result = repository.findByMemberId(1L);

		assertThat(result).hasSize(1);
	}

	@Test
	void existsByMemberIdAndProductId() {
		when(repository.existsByMemberIdAndProductId(1L, 2L))
			.thenReturn(true);

		boolean result = repository.existsByMemberIdAndProductId(1L, 2L);

		assertThat(result).isTrue();
	}

	@Test
	void search() {
		when(repository.findAll())
			.thenReturn(List.of(new Subscription()));

		var result = repository.findAll();

		assertThat(result).hasSize(1);
	}
}
