package com.hana8.hanaro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana8.hanaro.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	List<Account> findByMemberId(Long memberId);
}
