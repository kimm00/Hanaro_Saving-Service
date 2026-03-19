package com.hana8.hanaro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hana8.hanaro.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	@Query("""
			SELECT a FROM Account a
			WHERE a.member.id = :memberId
			AND a.id NOT IN (
				SELECT s.account.id FROM Subscription s
			)
		""")
	Optional<Account> findDefaultAccount(Long memberId);

	List<Account> findByMemberId(Long memberId);

	boolean existsByAccountNumber(String accountNumber);
}
