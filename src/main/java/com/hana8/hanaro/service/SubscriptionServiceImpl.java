package com.hana8.hanaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.Subscription;
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

	@Override
	public Subscription subscribe(Long memberId, Long productId, Long accountId) {
		if (subscriptionRepository.existsByMemberIdAndProductId(memberId, productId)) {
			throw new RuntimeException("이미 가입한 상품입니다.");
		}

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

		Account account = accountRepository.findById(accountId)
			.orElseThrow(() -> new RuntimeException("계좌가 존재하지 않습니다."));

		if (!account.getMember().getId().equals(memberId)) {
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

		return subscriptionRepository.save(subscription);
	}

	@Override
	public List<Subscription> getSubscriptions(Long memberId) {
		return subscriptionRepository.findByMemberId(memberId);
	}

	@Override
	public Subscription cancelSubscription(Long subscriptionId) {

		Subscription subscription = subscriptionRepository.findById(subscriptionId)
			.orElseThrow(() -> new RuntimeException("가입 내역이 존재하지 않습니다."));

		subscription.setCanceled(true);

		return subscriptionRepository.save(subscription);
	}
}
