package com.hana8.hanaro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hana8.hanaro.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	List<Account> findByMemberId(Long memberId);

	boolean existsByAccountNumber(String accountNumber);

	// 기본 계좌 조회 (Subscription이 없는 자유입출금 계좌)
	@Query("""
		SELECT a FROM Account a
		WHERE a.member.id = :memberId
		  AND a.status = 'ACTIVE'
		  AND NOT EXISTS (
		      SELECT s FROM Subscription s WHERE s.account = a
		  )
		""")
	Optional<Account> findDefaultAccount(@Param("memberId") Long memberId);
}
