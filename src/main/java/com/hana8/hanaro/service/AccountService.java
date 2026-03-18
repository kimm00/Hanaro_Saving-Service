package com.hana8.hanaro.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hana8.hanaro.common.enums.AccountStatus;
import com.hana8.hanaro.common.enums.ProductType;
import com.hana8.hanaro.dto.account.AccountResponseDTO;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.Subscription;
import com.hana8.hanaro.mapper.AccountMapper;
import com.hana8.hanaro.repository.AccountRepository;
import com.hana8.hanaro.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;
	private final MemberRepository memberRepository;
	private final AccountMapper accountMapper;

	// 회원가입 시 자유입출금 계좌 자동 생성
	public AccountResponseDTO createDefaultAccount(Long memberId) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

		String accountNumber = generateAccountNumber();

		Account account = Account.builder()
			.accountNumber(accountNumber)
			.member(member)
			.subscription(null) // 자유입출금 계좌는 상품이 없음
			.status(AccountStatus.ACTIVE)
			.balance(0L)
			.startDate(LocalDate.now())
			.build();

		return accountMapper.toDTO(accountRepository.save(account));
	}

	// 적금 납입
	public AccountResponseDTO makePayment(Long accountId) {

		Account account = accountRepository.findById(accountId).orElseThrow();
		Subscription sub = account.getSubscription();

		if (sub == null) {
			throw new IllegalArgumentException("해당 계좌는 납입 대상이 아닙니다.");
		}

		if (sub.isCanceled()) {
			throw new IllegalArgumentException("해지된 가입입니다.");
		}

		sub.setPaidCount(sub.getPaidCount() + 1);

		account.setBalance(
			account.getBalance() + sub.getPaymentAmount()
		);

		return accountMapper.toDTO(accountRepository.save(account));
	}

	// 계좌 목록 조회
	public List<AccountResponseDTO> getAccounts(Long memberId) {

		return accountRepository.findByMemberId(memberId)
			.stream()
			.map(account -> {
				AccountResponseDTO dto = accountMapper.toDTO(account);

				// 자유입출금 계좌 예외 처리
				if (account.getSubscription() == null) {
					dto.setCurrentInterest(0L);
					dto.setExpectedAmount(account.getBalance());
					return dto;
				}

				Subscription sub = account.getSubscription();
				Product product = sub.getProduct();

				Long interest;
				Long expected;

				// product 유형에 따라 이자 계산 방식 다름
				if (product.getProductType() == ProductType.DEPOSIT) {
					interest = calculateDepositInterest(account, sub);
					expected = account.getBalance() + interest;
				} else {
					long total = sub.getPaymentAmount() * sub.getPaidCount();
					interest = calculateSavingInterest(sub);
					expected = total + interest;
				}

				dto.setCurrentInterest(interest);
				dto.setExpectedAmount(expected);

				return dto;
			})
			.toList();
	}

	// 해지 전 계좌 상세 조회
	public AccountResponseDTO previewAccount(Long accountId) {
		Account account = accountRepository.findById(accountId)
			.orElseThrow(() -> new IllegalArgumentException("계좌 없음"));

		AccountResponseDTO dto = accountMapper.toDTO(account);

		// 자유입출금 예외 처리
		if (account.getSubscription() == null) {
			dto.setCurrentInterest(0L);
			dto.setExpectedAmount(account.getBalance());
			return dto;
		}

		Subscription sub = account.getSubscription();
		Product product = sub.getProduct();

		Long interest;
		Long expected;

		// product 유형에 따라 이자 계산 방식 다름
		if (product.getProductType() == ProductType.DEPOSIT) {
			interest = calculateDepositInterest(account, sub);
			expected = account.getBalance() + interest;
		} else {
			long total = sub.getPaymentAmount() * sub.getPaidCount();
			interest = calculateSavingInterest(sub);
			expected = total + interest;
		}

		dto.setCurrentInterest(interest);
		dto.setExpectedAmount(expected);

		return dto;
	}

	// 만기 / 중도 해지
	public AccountResponseDTO closeAccount(Long accountId) {

		Account account = accountRepository.findById(accountId)
			.orElseThrow(() -> new IllegalArgumentException("계좌가 존재하지 않습니다."));

		if (account.getStatus() != AccountStatus.ACTIVE) {
			throw new IllegalArgumentException("이미 해지된 계좌입니다.");
		}

		// 자유입출금 계좌는 해지 불가
		if (account.getSubscription() == null) {
			throw new IllegalArgumentException("자유입출금 계좌는 해지 대상이 아닙니다.");
		}

		Subscription sub = account.getSubscription();
		Product product = sub.getProduct();

		int target = getTargetCount(sub);

		Long finalAmount;
		Long interest;

		if (sub.getPaidCount() >= target) {

			if (product.getProductType() == ProductType.DEPOSIT) {
				interest = calculateDepositInterest(account, sub);
				finalAmount = account.getBalance() + interest;
			} else {
				long total = sub.getPaymentAmount() * sub.getPaidCount();
				interest = calculateSavingInterest(sub);
				finalAmount = total + interest;
			}

			account.setStatus(AccountStatus.COMPLETED);

		} else {

			double rate = product.getCancelRate() / 100.0;

			if (product.getProductType() == ProductType.DEPOSIT) {
				double months = calculateDepositMonths(sub);
				interest = (long)(account.getBalance() * rate * months / 12);
				finalAmount = account.getBalance() + interest;
			} else {
				long total = sub.getPaymentAmount() * sub.getPaidCount();
				interest = (long)(total * rate / 12); // ⭐ 단순화
				finalAmount = total + interest;
			}

			account.setStatus(AccountStatus.CANCELED);
		}

		account.setBalance(finalAmount);

		Account saved = accountRepository.save(account);

		AccountResponseDTO dto = accountMapper.toDTO(saved);
		dto.setCurrentInterest(interest);
		dto.setExpectedAmount(finalAmount);

		return dto;
	}

	// 계좌번호 생성
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

	// 예금 기간 계산
	private double calculateDepositMonths(Subscription sub) {
		return ChronoUnit.MONTHS.between(sub.getStartDate(), LocalDate.now());
	}

	// // 적금 기간 계산
	// private double convertToMonths(Account account) {
	//
	// 	int count = account.getPaidCount();
	//
	// 	switch (account.getProduct().getPaymentCycle()) {
	// 		case WEEKLY:
	// 			return count / 4.0;
	// 		case MONTHLY:
	// 			return count;
	// 		default:
	// 			throw new IllegalArgumentException("지원 안함");
	// 	}
	// }

	// 적금 이자
	private Long calculateSavingInterest(Subscription sub) {

		double rate = sub.getInterestRate() / 100.0;

		int n = sub.getPaidCount();
		long payment = sub.getPaymentAmount();

		return (long)(payment * n * (n + 1) / 2 * rate / 12);
	}

	// 예금 이자
	private Long calculateDepositInterest(Account account, Subscription sub) {

		double rate = sub.getInterestRate() / 100.0;

		double months = calculateDepositMonths(sub);

		return (long)(account.getBalance() * rate * months / 12);
	}

	// 목표 납입 횟수
	private int getTargetCount(Subscription sub) {

		int period = sub.getPeriod();

		switch (sub.getProduct().getPaymentCycle()) {
			case WEEKLY:
				return period * 4;
			case MONTHLY:
				return period;
			default:
				throw new IllegalArgumentException();
		}
	}
}
