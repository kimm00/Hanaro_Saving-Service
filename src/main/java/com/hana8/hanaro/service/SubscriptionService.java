package com.hana8.hanaro.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hana8.hanaro.common.enums.AccountStatus;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

	private final SubscriptionRepository subscriptionRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	private final AccountRepository accountRepository;
	private final SubscriptionMapper subscriptionMapper;

	public SubscriptionResponseDTO subscribe(SubscriptionRequestDTO dto, String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("회원 없음"));

		Product product = productRepository.findById(dto.getProductId())
			.orElseThrow();

		Subscription subscription = Subscription.builder()
			.member(member)
			.product(product)
			.paymentAmount(product.getPaymentAmount())
			.interestRate(product.getInterestRate())
			.period(product.getPeriod())
			.paidCount(0)
			.startDate(LocalDate.now())
			.canceled(false)
			.status(SubscriptionStatus.ACTIVE)
			.build();

		String accountNumber;

		if (dto.getAccountNumber() != null && !dto.getAccountNumber().isBlank()) {
			accountNumber = dto.getAccountNumber();

			if (accountRepository.existsByAccountNumber(accountNumber)) {
				throw new IllegalArgumentException("이미 존재하는 계좌번호입니다.");
			}
		} else {
			accountNumber = generateAccountNumber();
		}
		System.out.println("로그인 email: " + email);
		System.out.println("찾은 member id: " + member.getId());

		Account account = Account.builder()
			.accountNumber(accountNumber)
			.member(member)
			.balance(0L)
			.startDate(LocalDate.now())
			.status(AccountStatus.ACTIVE)
			.build();

		accountRepository.save(account);

		subscription.setAccount(account);

		Subscription savedSubscription = subscriptionRepository.save(subscription);

		return subscriptionMapper.toDTO(savedSubscription);
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

	public SubscriptionResponseDTO cancelSubscription(Long subscriptionId, String email) {

		Subscription subscription = subscriptionRepository.findById(subscriptionId)
			.orElseThrow(() -> new IllegalArgumentException("가입 내역이 존재하지 않습니다."));

		// 본인 확인
		if (!subscription.getMember().getEmail().equals(email)) {
			throw new org.springframework.security.access.AccessDeniedException("본인만 해지 가능");
		}

		subscription.setCanceled(true);
		subscription.setStatus(SubscriptionStatus.CANCELED);

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
			rate = s.getProduct().getCancelRate() / 100.0;
		} else {
			rate = s.getInterestRate() / 100.0;
		}

		int n = s.getPaidCount();
		long payment = s.getPaymentAmount();

		return (long)(payment * n * (n + 1) / 2 * rate / 12);
	}

	// 검색 기능 - 회원 이름으로 가입 내역 검색
	public List<SubscriptionResponseDTO> getMySubscriptions(String email) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("회원 없음"));

		return getSubscriptions(member.getId()); // 기존 메서드 재사용 👍
	}

	public List<SubscriptionResponseDTO> adminSearch(Long memberId, String status) {

		List<Subscription> list = subscriptionRepository.findAll();

		return list.stream()
			.filter(s -> memberId == null || s.getMember().getId().equals(memberId))
			.filter(s -> status == null || s.getStatus().name().equalsIgnoreCase(status))
			.map(s -> {
				SubscriptionResponseDTO dto = subscriptionMapper.toDTO(s);
				dto.setCurrentInterest(calculateInterest(s));
				return dto;
			})
			.toList();
	}

	public SubscriptionResponseDTO matureSubscription(Long subscriptionId) {

		Subscription subscription = subscriptionRepository.findById(subscriptionId)
			.orElseThrow(() -> new IllegalArgumentException("가입 내역 없음"));

		// 상태 체크
		if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
			throw new IllegalStateException("이미 처리된 상품입니다.");
		}

		Product product = subscription.getProduct();
		Account account = subscription.getAccount();

		// 이자 계산
		long interest = calculateInterest(subscription);
		long totalAmount;

		// 원금 + 이자
		if (product.getProductType() == ProductType.DEPOSIT) {
			totalAmount = account.getBalance() + interest;
		} else {
			totalAmount = subscription.getPaymentAmount() * subscription.getPaidCount() + interest;
		}

		// 계좌 입금
		account.setBalance(account.getBalance() + totalAmount);

		account.setStatus(AccountStatus.CLOSED);

		// 상태 변경
		subscription.setStatus(SubscriptionStatus.COMPLETED);

		accountRepository.save(account);
		
		Subscription saved = subscriptionRepository.save(subscription);

		SubscriptionResponseDTO dto = subscriptionMapper.toDTO(saved);
		dto.setCurrentInterest(interest);

		return dto;
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
