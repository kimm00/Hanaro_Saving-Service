package com.hana8.hanaro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hana8.hanaro.dto.account.AccountResponseDTO;
import com.hana8.hanaro.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService;

	// 회원가입 시 자유입출금 계좌 자동 생성
	@PostMapping("/default")
	public AccountResponseDTO createDefaultAccount(@RequestParam Long memberId) {
		return accountService.createDefaultAccount(memberId);
	}

	// 상품 가입용 계좌 생성 (계좌번호 직접 입력)
	@PostMapping
	public AccountResponseDTO createAccount(
		@RequestParam Long memberId,
		@RequestParam String accountNumber) {

		return accountService.createAccountWithNumber(memberId, accountNumber);
	}

	// 내 가입 계좌 조회
	@GetMapping("/member/{memberId}")
	public List<AccountResponseDTO> getAccount(@PathVariable Long memberId) {
		return accountService.getAccounts(memberId);
	}

	// 중도 해지
	@PatchMapping("/{accountId}/cancel")
	public AccountResponseDTO cancelAccount(@PathVariable Long accountId) {
		return accountService.cancelAccount(accountId);
	}
}
