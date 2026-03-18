package com.hana8.hanaro.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hana8.hanaro.dto.account.AccountResponseDTO;
import com.hana8.hanaro.service.AccountService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService;

	// 회원가입 시 자유입출금 계좌 자동 생성
	@PostMapping("/default-account")
	public AccountResponseDTO createDefaultAccount(@RequestParam @NotNull Long memberId) {
		return accountService.createDefaultAccount(memberId);
	}

	// 내 가입 계좌 조회
	@GetMapping("/members/{memberId}/accounts")
	public List<AccountResponseDTO> getAccountsByMember(@PathVariable @NotNull Long memberId) {
		return accountService.getAccounts(memberId);
	}

	// 관리자 만기 처리
	@PatchMapping("/admin/{accountId}/close")
	public AccountResponseDTO adminCloseAccount(@PathVariable Long accountId) {
		return accountService.closeAccount(accountId);
	}

	// 계좌 상세 조회
	@GetMapping("/{accountId}")
	public AccountResponseDTO getAccountDetail(@PathVariable Long accountId) {
		return accountService.previewAccount(accountId);
	}

	// 적금 납입
	@PostMapping("/{accountId}/payment")
	public AccountResponseDTO makePayment(@PathVariable Long accountId) {
		return accountService.makePayment(accountId);
	}

	// 만기 및 중도 해지
	@PatchMapping("/{accountId}/close")
	public AccountResponseDTO closeAccount(@PathVariable Long accountId) {
		return accountService.closeAccount(accountId);
	}
}
