package com.hana8.hanaro.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
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

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService;

	// 회원가입 시 자유입출금 계좌 자동 생성
	@PostMapping("/default-account")
	public AccountResponseDTO createDefaultAccount(
		@RequestParam("memberId") Long memberId
	) {
		return accountService.createDefaultAccount(memberId);
	}

	// 내 가입 계좌 조회
	@GetMapping("/members/{memberId}")
	public List<AccountResponseDTO> getAccountsByMember(
		@PathVariable("memberId") Long memberId
	) {
		return accountService.getAccounts(memberId);
	}

	// 계좌 상세 조회
	@GetMapping("/{accountId}")
	public AccountResponseDTO getAccountDetail(
		@PathVariable("accountId") Long accountId
	) {
		return accountService.previewAccount(accountId);
	}

	// 적금 납입
	@PostMapping("/{accountId}/payment")
	public AccountResponseDTO makePayment(
		@PathVariable("accountId") Long accountId
	) {
		return accountService.makePayment(accountId);
	}

	// 사용자 중도 해지
	@PatchMapping("/{accountId}/cancel")
	public AccountResponseDTO cancelAccount(
		@PathVariable("accountId") Long accountId
	) {
		return accountService.cancelAccount(accountId);
	}

	// 관리자: 만기 처리
	@PatchMapping("/admin/{accountId}/complete")
	@PreAuthorize("hasRole('ADMIN')")
	public AccountResponseDTO completeAccount(
		@PathVariable("accountId") Long accountId
	) {
		return accountService.completeAccount(accountId);
	}
}
