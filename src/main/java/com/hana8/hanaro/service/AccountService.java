package com.hana8.hanaro.service;

import java.util.List;

import com.hana8.hanaro.entity.Account;

public interface AccountService {
	Account createAccount(Long memberId, Long ProductId, String accountNumber);

	List<Account> getAccounts(Long memberId);

	Account cancelAccount(Long accountId);
}
