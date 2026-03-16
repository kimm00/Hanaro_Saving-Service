package com.hana8.hanaro.service;

import java.util.List;

import com.hana8.hanaro.entity.Account;

public interface AccountService {
	// 회원가입 시 자유입출금 계좌 생성
	Account createDefaultAccount(Long memberId);

	// 상품 가입 시 계좌번호 직접 입력
	Account createAccountWithNumber(Long memberId, String accountNumber);

	List<Account> getAccounts(Long memberId);

	Account cancelAccount(Long accountId);
}
