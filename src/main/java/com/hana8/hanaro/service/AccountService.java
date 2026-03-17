package com.hana8.hanaro.service;

import java.util.List;

import com.hana8.hanaro.dto.account.AccountResponseDTO;

public interface AccountService {
	// 회원가입 시 자유입출금 계좌 생성
	AccountResponseDTO createDefaultAccount(Long memberId);

	AccountResponseDTO createAccountWithNumber(Long memberId, String accountNumber);

	List<AccountResponseDTO> getAccounts(Long memberId);

	AccountResponseDTO cancelAccount(Long accountId);
}
