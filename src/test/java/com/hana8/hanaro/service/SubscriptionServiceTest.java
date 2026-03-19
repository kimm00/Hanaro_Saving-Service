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

import com.hana8.hanaro.common.enums.ProductType;
import com.hana8.hanaro.common.enums.SubscriptionStatus;
import com.hana8.hanaro.dto.subscription.SubscriptionRequestDTO;
import com.hana8.hanaro.dto.subscription.SubscriptionResponseDTO;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.Subscription;
import com.hana8.hanaro.mapper.SubscriptionMapper;
import com.hana8.hanaro.repository.AccountRepository;
import com.hana8.hanaro.repository.MemberRepository;
import com.hana8.hanaro.repository.ProductRepository;
import com.hana8.hanaro.repository.SubscriptionRepository;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

	@Mock
	private SubscriptionRepository subscriptionRepository;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private ProductRepository productRepository;
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private SubscriptionMapper subscriptionMapper;

	@InjectMocks
	private SubscriptionService subscriptionService;

	@Test
	void subscribe() {
		SubscriptionRequestDTO dto = mock(SubscriptionRequestDTO.class);
		assertThrows(Exception.class,
			() -> subscriptionService.subscribe(dto));
	}

	@Test
	void subscribe_success() {

		// given
		SubscriptionRequestDTO dto = new SubscriptionRequestDTO();
		dto.setMemberId(1L);
		dto.setProductId(1L);
		dto.setAccountNumber(null);

		Member member = Member.builder()
			.id(1L)
			.email("test@test.com")
			.build();

		Product product = Product.builder()
			.id(1L)
			.paymentAmount(1000L)
			.interestRate(2.0)
			.period(12)
			.build();

		when(memberRepository.findById(1L))
			.thenReturn(Optional.of(member));

		when(productRepository.findById(1L))
			.thenReturn(Optional.of(product));

		when(accountRepository.existsByAccountNumber(anyString()))
			.thenReturn(false);

		when(accountRepository.save(any()))
			.thenAnswer(invocation -> invocation.getArgument(0));

		when(subscriptionRepository.save(any()))
			.thenAnswer(invocation -> invocation.getArgument(0));

		when(subscriptionMapper.toDTO(any()))
			.thenReturn(new SubscriptionResponseDTO());

		// when
		SubscriptionResponseDTO result = subscriptionService.subscribe(dto);

		// then
		assertNotNull(result);
		verify(subscriptionRepository).save(any());
	}

	@Test
	void getSubscriptions() {
		subscriptionService.getSubscriptions(1L);
		verify(subscriptionRepository).findByMemberId(1L);
	}

	@Test
	void cancelSubscription() {
		assertThrows(Exception.class,
			() -> subscriptionService.cancelSubscription(1L));
	}

	@Test
	void cancelSubscription_success() {

		Subscription sub = Subscription.builder()
			.id(1L)
			.canceled(false)
			.status(SubscriptionStatus.ACTIVE)
			.paymentAmount(1000L)
			.interestRate(2.0)
			.period(12)
			.product(Product.builder().cancelRate(1.0).build())
			.build();

		when(subscriptionRepository.findById(1L))
			.thenReturn(Optional.of(sub));

		when(subscriptionRepository.save(any()))
			.thenAnswer(invocation -> invocation.getArgument(0));

		when(subscriptionMapper.toDTO(any()))
			.thenReturn(new SubscriptionResponseDTO());

		SubscriptionResponseDTO result = subscriptionService.cancelSubscription(1L);

		assertNotNull(result);
		assertTrue(sub.isCanceled());
	}

	@Test
	void getMySubscriptions() {
		assertThrows(Exception.class,
			() -> subscriptionService.getMySubscriptions("test@test.com"));
	}

	@Test
	void getMySubscriptions_success() {

		Member member = Member.builder()
			.id(1L)
			.email("test@test.com")
			.build();

		when(memberRepository.findByEmail("test@test.com"))
			.thenReturn(Optional.of(member));

		when(subscriptionRepository.findByMemberId(1L))
			.thenReturn(List.of());

		List<?> result = subscriptionService.getMySubscriptions("test@test.com");

		assertNotNull(result);
	}

	@Test
	void adminSearch() {
		subscriptionService.adminSearch(null, null);
		verify(subscriptionRepository).findAll();
	}

	@Test
	void matureSubscription() {
		assertThrows(Exception.class,
			() -> subscriptionService.matureSubscription(1L));
	}

	@Test
	void matureSubscription_success() {

		Product product = Product.builder()
			.productType(ProductType.DEPOSIT)
			.cancelRate(1.0)
			.build();

		Account account = Account.builder()
			.balance(1000L)
			.build();

		Subscription sub = Subscription.builder()
			.id(1L)
			.status(SubscriptionStatus.ACTIVE)
			.paymentAmount(1000L)
			.interestRate(2.0)
			.period(12)
			.paidCount(1)
			.product(product)
			.account(account)
			.build();

		when(subscriptionRepository.findById(1L))
			.thenReturn(Optional.of(sub));

		when(subscriptionRepository.save(any()))
			.thenAnswer(invocation -> invocation.getArgument(0));

		when(subscriptionMapper.toDTO(any()))
			.thenReturn(new SubscriptionResponseDTO());

		SubscriptionResponseDTO result = subscriptionService.matureSubscription(1L);

		assertNotNull(result);
	}
}
