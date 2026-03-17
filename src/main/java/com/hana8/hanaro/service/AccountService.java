package com.hana8.hanaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hana8.hanaro.common.enums.AccountStatus;
import com.hana8.hanaro.dto.account.AccountResponseDTO;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.mapper.AccountMapper;
import com.hana8.hanaro.repository.AccountRepository;
import com.hana8.hanaro.repository.MemberRepository;
import com.hana8.hanaro.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;
	private final MemberRepository memberRepository;
	private final AccountMapper accountMapper;
	private final ProductRepository productRepository;

	// 회원가입 시 자유입출금 계좌 자동 생성
	public AccountResponseDTO createDefaultAccount(Long memberId) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

		String accountNumber = generateAccountNumber();

		Account account = Account.builder()
			.accountNumber(accountNumber)
			.member(member)
			.product(null) // 자유입출금 계좌는 상품이 없음
			.status(AccountStatus.ACTIVE)
			.balance(0L)
			.paymentAmount(0L)
			.interestRate(0.0)
			.build();

		return accountMapper.toDTO(accountRepository.save(account));
	}

	// 상품 가입 시 계좌번호 입력
	public AccountResponseDTO createAccountWithNumber(Long memberId, Long productId, String accountNumber) {

		if (!accountNumber.matches("\\d{3}-\\d{4}-\\d{4}")) {
			throw new RuntimeException("계좌번호 형식이 올바르지 않습니다.");
		}

		if (accountRepository.existsByAccountNumber(accountNumber)) {
			throw new RuntimeException("이미 존재하는 계좌번호입니다.");
		}

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

		Account account = Account.builder()
			.accountNumber(accountNumber)
			.member(member)
			.product(product)
			.status(AccountStatus.ACTIVE)
			.balance(0L)
			.paymentAmount(0L)
			.interestRate(product.getInterestRate())
			.cancelRate(product.getCancelRate())
			.build();

		return accountMapper.toDTO(accountRepository.save(account));
	}

	public List<AccountResponseDTO> getAccounts(Long memberId) {

		return accountRepository.findByMemberId(memberId)
			.stream()
			.map(accountMapper::toDTO)
			.toList();
	}

	public AccountResponseDTO cancelAccount(Long accountId) {

		Account account = accountRepository.findById(accountId)
			.orElseThrow(() -> new RuntimeException("계좌가 존재하지 않습니다."));

		account.setStatus(AccountStatus.CANCELED);

		return accountMapper.toDTO(accountRepository.save(account));
	}

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
