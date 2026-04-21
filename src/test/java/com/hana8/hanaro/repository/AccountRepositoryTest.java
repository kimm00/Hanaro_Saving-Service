package com.hana8.hanaro.repository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hana8.hanaro.entity.Account;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryTest {

	@Mock
	AccountRepository accountRepository;

	@Test
	void findDefaultAccount() {
		Long memberId = 1L;

		Account account = Account.builder().id(1L).build();

		when(accountRepository.findDefaultAccount(memberId))
			.thenReturn(Optional.of(account));

		Optional<Account> result = accountRepository.findDefaultAccount(memberId);

		assertThat(result).isPresent();
		verify(accountRepository).findDefaultAccount(memberId);
	}

	@Test
	void findByMemberId() {
		Long memberId = 1L;

		when(accountRepository.findByMemberId(memberId))
			.thenReturn(List.of(new Account()));

		var result = accountRepository.findByMemberId(memberId);

		assertThat(result).hasSize(1);
	}

	@Test
	void existsByAccountNumber() {
		when(accountRepository.existsByAccountNumber("123"))
			.thenReturn(true);

		boolean result = accountRepository.existsByAccountNumber("123");

		assertThat(result).isTrue();
	}
}
