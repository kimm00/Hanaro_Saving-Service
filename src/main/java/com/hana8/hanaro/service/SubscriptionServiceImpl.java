package com.hana8.hanaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
public class SubscriptionServiceImpl implements SubscriptionService {

	private final SubscriptionRepository subscriptionRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	private final AccountRepository accountRepository;
	private final SubscriptionMapper subscriptionMapper;

	@Override
	public SubscriptionResponseDTO subscribe(SubscriptionRequestDTO dto) {
		if (subscriptionRepository.existsByMemberIdAndProductId(dto.getMemberId(), dto.getProductId())) {
			throw new RuntimeException("이미 가입한 상품입니다.");
		}

		Member member = memberRepository.findById(dto.getMemberId())
			.orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

		Product product = productRepository.findById(dto.getProductId())
			.orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

		Account account = accountRepository.findById(dto.getAccountId())
			.orElseThrow(() -> new RuntimeException("계좌가 존재하지 않습니다."));

		if (!account.getMember().getId().equals(dto.getMemberId())) {
			throw new RuntimeException("본인의 계좌만 사용할 수 있습니다.");
		}

		Subscription subscription = Subscription.builder()
			.member(member)
			.product(product)
			.account(account)
			.interestRate(product.getInterestRate())
			.period(product.getPeriod())
			.paymentAmount(0L)
			.canceled(false)
			.build();

		Subscription saved = subscriptionRepository.save(subscription);
		SubscriptionResponseDTO response = subscriptionMapper.toDTO(saved);
		response.setCurrentInterest(calculateInterest(saved));
		return response;
	}

	@Override
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

	@Override
	public SubscriptionResponseDTO cancelSubscription(Long subscriptionId) {

		Subscription subscription = subscriptionRepository.findById(subscriptionId)
			.orElseThrow(() -> new RuntimeException("가입 내역이 존재하지 않습니다."));

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
}
