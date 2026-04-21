package com.hana8.hanaro.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hana8.hanaro.common.enums.AccountStatus;
import com.hana8.hanaro.common.enums.ProductType;
import com.hana8.hanaro.common.enums.SubscriptionStatus;
import com.hana8.hanaro.dto.account.AccountResponseDTO;
import com.hana8.hanaro.entity.Account;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.Subscription;
import com.hana8.hanaro.mapper.AccountMapper;
import com.hana8.hanaro.repository.AccountRepository;
import com.hana8.hanaro.repository.MemberRepository;
import com.hana8.hanaro.repository.SubscriptionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;
	private final MemberRepository memberRepository;
	private final AccountMapper accountMapper;
	private final SubscriptionRepository subscriptionRepository;

	// 회원가입 시 자유입출금 계좌 자동 생성
	public AccountResponseDTO createDefaultAccount(Long memberId) {
		log.info("계좌 생성: memberId={}", memberId);

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

		String accountNumber = generateAccountNumber();

		Account account = Account.builder()
			.accountNumber(accountNumber)
			.member(member)
			.status(AccountStatus.ACTIVE)
			.balance(0L)
			.startDate(LocalDate.now())
			.build();

		return accountMapper.toDTO(accountRepository.save(account));
	}

	// 적금 납입
	public AccountResponseDTO makePayment(Long accountId) {
		log.info("납입 발생: accountId={}", accountId);

		Account account = accountRepository.findById(accountId).orElseThrow();

		Subscription sub = subscriptionRepository.findByAccountId(account.getId())
			.orElseThrow(() -> new IllegalArgumentException("가입 정보가 없습니다."));

		if (account.getStatus() == AccountStatus.CLOSED) {
			throw new IllegalArgumentException("이미 종료된 계좌입니다.");
		}

		// 납입
		if (sub.getProduct().getProductType() != ProductType.SAVING) {
			throw new IllegalArgumentException("적금만 납입 가능합니다.");
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

				// subscription 조회
				Subscription sub = subscriptionRepository.findByAccountId(account.getId())
					.orElse(null);

				// 자유입출금 계좌 처리
				if (sub == null) {
					dto.setCurrentInterest(0L);
					dto.setExpectedAmount(account.getBalance());
					return dto;
				}

				Product product = sub.getProduct();

				AccountStatus status = account.getStatus();

				dto.setStatus(status);

				Long interest;
				Long expected;

				// product 유형에 따라 이자 계산 방식 다름ㅇ
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

	// 계좌 상세 조회
	public AccountResponseDTO previewAccount(Long accountId) {
		Account account = accountRepository.findById(accountId)
			.orElseThrow(() -> new IllegalArgumentException("계좌 없음"));

		AccountResponseDTO dto = accountMapper.toDTO(account);

		Subscription sub = subscriptionRepository.findByAccountId(account.getId())
			.orElse(null);

		// 자유입출금 처리
		if (sub == null) {
			dto.setCurrentInterest(0L);
			dto.setExpectedAmount(account.getBalance());
			return dto;
		}

		Product product = sub.getProduct();

		AccountStatus status = account.getStatus();

		// 만기 반영
		int target = getTargetCount(sub);
		if (sub.getPaidCount() >= target) {
			status = AccountStatus.CLOSED;
		}

		dto.setStatus(status);

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

	// 사용자 중도 해지
	public AccountResponseDTO cancelAccount(Long accountId) {
		log.info("중도 해지: accountId={}", accountId);

		Account account = accountRepository.findById(accountId)
			.orElseThrow(() -> new IllegalArgumentException("계좌가 존재하지 않습니다."));

		if (account.getStatus() != AccountStatus.ACTIVE) {
			throw new IllegalArgumentException("이미 해지된 계좌입니다.");
		}

		Subscription sub = subscriptionRepository.findByAccountId(account.getId())
			.orElseThrow(() -> new IllegalArgumentException("가입 정보가 없습니다."));

		int target = getTargetCount(sub);

		// 만기면 cancel 못하게
		if (sub.getPaidCount() >= target) {
			throw new IllegalArgumentException("이미 만기된 계좌입니다.");
		}

		return processClose(account, sub, false); // false = 중도해지
	}

	// 관리자 만기 처리
	public AccountResponseDTO completeAccount(Long accountId) {
		log.info("만기 처리: accountId={}", accountId);

		Account account = accountRepository.findById(accountId)
			.orElseThrow(() -> new IllegalArgumentException("계좌가 존재하지 않습니다."));

		Subscription sub = subscriptionRepository.findByAccountId(account.getId())
			.orElseThrow(() -> new IllegalArgumentException("가입 정보가 없습니다."));

		int target = getTargetCount(sub);

		// 만기 아닐 때 막기
		if (sub.getPaidCount() < target) {
			throw new IllegalArgumentException("아직 만기되지 않았습니다.");
		}

		return processClose(account, sub, true); // true = 만기
	}

	// 해지 / 만기 공통 처리 로직
	private AccountResponseDTO processClose(Account account, Subscription sub, boolean isMature) {

		Product product = sub.getProduct();

		Long finalAmount;
		Long interest;

		if (isMature) {
			sub.setStatus(SubscriptionStatus.COMPLETED);

			// 만기
			if (product.getProductType() == ProductType.DEPOSIT) {
				interest = calculateDepositInterest(account, sub);
				finalAmount = account.getBalance() + interest;
			} else {
				long total = sub.getPaymentAmount() * sub.getPaidCount();
				interest = calculateSavingInterest(sub);
				finalAmount = total + interest;
			}
		} else {
			sub.setStatus(SubscriptionStatus.CANCELED);

			// 중도 해지
			double rate = product.getCancelRate() / 100.0;

			if (product.getProductType() == ProductType.DEPOSIT) {
				double months = calculateDepositMonths(sub);
				interest = (long)(account.getBalance() * rate * months / 12);
				finalAmount = account.getBalance() + interest;
			} else {
				long total = sub.getPaymentAmount() * sub.getPaidCount();
				interest = (long)(total * rate / 12);
				finalAmount = total + interest;
			}
		}

		// 돈 이체
		Account defaultAccount = accountRepository
			.findDefaultAccount(account.getMember().getId())
			.orElseThrow(() -> new IllegalArgumentException("기본 계좌 없음"));

		defaultAccount.setBalance(defaultAccount.getBalance() + finalAmount);

		// 가입 계좌 정리
		account.setStatus(AccountStatus.CLOSED);
		account.setBalance(0L);

		// 저장
		subscriptionRepository.save(sub);
		accountRepository.save(defaultAccount);
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

		if (sub.getProduct().getProductType() == ProductType.DEPOSIT) {
			return 1;
		}

		return sub.getPeriod();
	}
}
