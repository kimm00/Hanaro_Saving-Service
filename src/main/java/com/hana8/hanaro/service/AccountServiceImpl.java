package com.hana8.hanaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hana8.hanaro.common.enums.AccountStatus;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.repository.AccountRepository;
import com.hana8.hanaro.repository.MemberRepository;
import com.hana8.hanaro.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	private final AccountRepository accountRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;

	@Override
	public Account createAccount(Long memberId, Long productId, String accountNumber) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

		Account account = Account.builder()
			.accountNumber(accountNumber)
			.member(member)
			.product(product)
			.interestRate(product.getInterestRate())
			.status(AccountStatus.ACTIVE)
			.balance(0L)
			.build();

		return accountRepository.save(account);
	}

	@Override
	public List<Account> getAccounts(Long memberId) {
		return accountRepository.findByMemberId(memberId);
	}

	@Override
	public Account cancelAccount(Long accountId) {
		Account account = accountRepository.findById(accountId)
			.orElseThrow(() -> new RuntimeException("계좌가 존재하지 않습니다."));

		account.setStatus(AccountStatus.CANCELED);

		return accountRepository.save(account);
	}
}
