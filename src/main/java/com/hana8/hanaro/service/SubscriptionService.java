package com.hana8.hanaro.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hana8.hanaro.common.enums.AccountStatus;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

	private final SubscriptionRepository subscriptionRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	private final AccountRepository accountRepository;
	private final SubscriptionMapper subscriptionMapper;

	public SubscriptionResponseDTO subscribe(SubscriptionRequestDTO dto) {
		Member member = memberRepository.findById(dto.getMemberId()).orElseThrow();
		Product product = productRepository.findById(dto.getProductId()).orElseThrow();

		// Subscription 생성
		Subscription subscription = Subscription.builder()
			.member(member)
			.product(product)
			.paymentAmount(product.getPaymentAmount())
			.interestRate(product.getInterestRate())
			.period(product.getPeriod())
			.paidCount(0)
			.startDate(LocalDate.now())
			.canceled(false)
			.build();

		subscription = subscriptionRepository.save(subscription);

		// 계좌번호 결정
		String accountNumber;

		if (dto.getAccountNumber() != null && !dto.getAccountNumber().isBlank()) {

			accountNumber = dto.getAccountNumber();

			// 중복 체크
			if (accountRepository.existsByAccountNumber(accountNumber)) {
				throw new IllegalArgumentException("이미 존재하는 계좌번호입니다.");
			}

		} else {
			accountNumber = generateAccountNumber();
		}

		// Account 생성
		Account account = Account.builder()
			.accountNumber(accountNumber)
			.member(member)
			.subscription(subscription)
			.balance(0L)
			.startDate(LocalDate.now())
			.status(AccountStatus.ACTIVE)
			.build();

		account = accountRepository.save(account);

		// 양방향 연결
		subscription.setAccount(account);
		subscriptionRepository.save(subscription);

		return subscriptionMapper.toDTO(subscription);
	}

	public List<SubscriptionResponseDTO> getSubscriptions(Long memberId) {
		return subscriptionRepository.findByMemberId(memberId)
			.stream()
			.map(s -> {
				SubscriptionResponseDTO dto = subscriptionMapper.toDTO(s);
				dto.setCurrentInterest(calculateInterest(s));
				return dto;
			})
			.toList();
	}

	public SubscriptionResponseDTO cancelSubscription(Long subscriptionId) {

		Subscription subscription = subscriptionRepository.findById(subscriptionId)
			.orElseThrow(() -> new IllegalArgumentException("가입 내역이 존재하지 않습니다."));

		subscription.setCanceled(true);

		Subscription saved = subscriptionRepository.save(subscription);
		SubscriptionResponseDTO dto = subscriptionMapper.toDTO(saved);
		dto.setCurrentInterest(calculateInterest(saved));
		return dto;
	}

	private Long calculateInterest(Subscription s) {
		if (s.getPaymentAmount() == null || s.getInterestRate() == null) {
			return 0L;
		}

		double rate;

		if (s.isCanceled()) {
			rate = s.getProduct().getCancelRate(); // 중도 해지 금리
		} else {
			rate = s.getInterestRate(); // 정상 금리
		}

		return (long)(s.getPaymentAmount() * rate * s.getPeriod());
	}

	// 검색 기능 - 회원 이름으로 가입 내역 검색
	public List<SubscriptionResponseDTO> searchSubscriptions(String keyword) {

		List<Subscription> result = subscriptionRepository.search(keyword); // 🔥 수정

		if (result.isEmpty()) {
			throw new IllegalArgumentException("검색 결과가 없습니다.");
		}

		return result.stream()
			.map(s -> {
				SubscriptionResponseDTO dto = subscriptionMapper.toDTO(s);
				dto.setCurrentInterest(calculateInterest(s));
				return dto;
			})
			.toList();
	}

	// 계좌 생성
	private String generateAccountNumber() {

		String accountNumber;

		do {
			int part1 = (int)(Math.random() * 900) + 100;
			int part2 = (int)(Math.random() * 9000) + 1000;
			int part3 = (int)(Math.random() * 9000) + 1000;

			accountNumber = part1 + "-" + part2 + "-" + part3;

		} while (accountRepository.existsByAccountNumber(accountNumber));

		return accountNumber;
	}
}
