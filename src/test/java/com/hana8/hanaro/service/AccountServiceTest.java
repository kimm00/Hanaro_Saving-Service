package com.hana8.hanaro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hana8.hanaro.common.enums.AccountStatus;
import com.hana8.hanaro.common.enums.PaymentCycle;
import com.hana8.hanaro.common.enums.ProductType;
import com.hana8.hanaro.dto.account.AccountResponseDTO;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.Subscription;
import com.hana8.hanaro.mapper.AccountMapper;
import com.hana8.hanaro.repository.AccountRepository;
import com.hana8.hanaro.repository.MemberRepository;
import com.hana8.hanaro.repository.SubscriptionRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	@Mock
	private AccountRepository accountRepository;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private SubscriptionRepository subscriptionRepository;
	@Mock
	private AccountMapper accountMapper;

	@InjectMocks
	private AccountService accountService;

	@Test
	void createDefaultAccount() {
		Member member = Member.builder().id(1L).build();

		when(memberRepository.findById(1L))
			.thenReturn(Optional.of(member));

		when(accountRepository.existsByAccountNumber(anyString()))
			.thenReturn(false);

		when(accountRepository.save(any()))
			.thenAnswer(invocation -> invocation.getArgument(0));

		when(accountMapper.toDTO(any()))
			.thenReturn(mock(AccountResponseDTO.class));

		accountService.createDefaultAccount(1L);

		verify(accountRepository).save(any());
	}

	@Test
	void makePayment() {
		assertThrows(Exception.class, () -> accountService.makePayment(1L));
	}

	@Test
	void makePayment_success() {

		Account account = Account.builder()
			.id(1L)
			.balance(10000L)
			.status(AccountStatus.ACTIVE)
			.build();

		Product product = Product.builder()
			.productType(ProductType.SAVING)
			.build();

		Subscription sub = Subscription.builder()
			.product(Product.builder()
				.productType(ProductType.SAVING)
				.paymentCycle(PaymentCycle.MONTHLY)
				.build())
			.paymentAmount(1000L)
			.paidCount(1)
			.period(12)
			.build();

		when(accountRepository.findById(any()))
			.thenReturn(Optional.of(account));

		when(subscriptionRepository.findByAccountId(any()))
			.thenReturn(Optional.of(sub));

		when(accountRepository.save(any()))
			.thenAnswer(invocation -> invocation.getArgument(0));

		when(accountMapper.toDTO(any()))
			.thenReturn(mock(AccountResponseDTO.class));

		AccountResponseDTO result = accountService.makePayment(1L);

		assertNotNull(result);
	}

	@Test
	void makePayment_closedAccount() {

		Account account = Account.builder()
			.id(1L)
			.status(AccountStatus.CLOSED)
			.build();

		when(accountRepository.findById(1L))
			.thenReturn(Optional.of(account));

		when(subscriptionRepository.findByAccountId(1L))
			.thenReturn(Optional.of(mock(Subscription.class)));

		assertThrows(IllegalArgumentException.class,
			() -> accountService.makePayment(1L));
	}

	@Test
	void getAccounts() {
		accountService.getAccounts(1L);
		verify(accountRepository).findByMemberId(1L);
	}

	@Test
	void getAccounts_noSubscription() {

		Account account = Account.builder()
			.id(1L)
			.balance(1000L)
			.build();

		when(accountRepository.findByMemberId(1L))
			.thenReturn(List.of(account));

		when(subscriptionRepository.findByAccountId(1L))
			.thenReturn(Optional.empty());

		when(accountMapper.toDTO(any()))
			.thenReturn(mock(AccountResponseDTO.class));

		accountService.getAccounts(1L);

		verify(accountRepository).findByMemberId(1L);
	}

	@Test
	void previewAccount() {
		assertThrows(Exception.class, () -> accountService.previewAccount(1L));
	}

	@Test
	void previewAccount_success() {
		Account account = Account.builder()
			.id(1L)
			.build();

		when(accountRepository.findById(1L))
			.thenReturn(Optional.of(account));

		when(accountMapper.toDTO(any()))
			.thenReturn(mock(AccountResponseDTO.class));

		accountService.previewAccount(1L);
	}

	@Test
	void cancelAccount() {
		assertThrows(Exception.class, () -> accountService.cancelAccount(1L));
	}

	@Test
	void cancelAccount_success() {

		Account account = Account.builder()
			.id(1L)
			.balance(0L)
			.member(Member.builder().id(1L).build())
			.status(AccountStatus.ACTIVE)
			.build();

		Product product = Product.builder()
			.productType(ProductType.SAVING)
			.paymentCycle(PaymentCycle.MONTHLY)
			.cancelRate(10.0)
			.build();

		Subscription sub = Subscription.builder()
			.product(product)
			.paidCount(1)
			.paymentAmount(1000L)
			.period(12)
			.build();

		Account defaultAccount = Account.builder()
			.member(Member.builder().id(1L).build())
			.balance(0L)
			.build();

		when(accountRepository.findById(1L))
			.thenReturn(Optional.of(account));

		when(subscriptionRepository.findByAccountId(1L))
			.thenReturn(Optional.of(sub));

		when(accountRepository.findDefaultAccount(anyLong()))
			.thenReturn(Optional.of(defaultAccount));

		when(accountRepository.save(any()))
			.thenAnswer(invocation -> invocation.getArgument(0));

		when(accountMapper.toDTO(any()))
			.thenReturn(mock(AccountResponseDTO.class));

		accountService.cancelAccount(1L);

		verify(accountRepository, atLeastOnce()).save(any());
	}

	@Test
	void completeAccount() {
		assertThrows(Exception.class, () -> accountService.completeAccount(1L));
	}

	@Test
	void completeAccount_notMature() {

		Account account = Account.builder()
			.id(1L)
			.build();

		Product product = Product.builder()
			.productType(ProductType.SAVING)
			.paymentCycle(PaymentCycle.MONTHLY)
			.build();

		Subscription sub = Subscription.builder()
			.paidCount(1)
			.period(12)
			.product(product)
			.build();

		when(accountRepository.findById(1L))
			.thenReturn(Optional.of(account));

		when(subscriptionRepository.findByAccountId(1L))
			.thenReturn(Optional.of(sub));

		assertThrows(IllegalArgumentException.class,
			() -> accountService.completeAccount(1L));
	}
}
