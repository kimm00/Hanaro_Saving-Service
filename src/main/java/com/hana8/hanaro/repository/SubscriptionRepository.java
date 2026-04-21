package com.hana8.hanaro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hana8.hanaro.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	Optional<Subscription> findByAccountId(Long accountId);

	List<Subscription> findByMemberId(Long memberId);

	// 중복 가입 방지
	boolean existsByMemberIdAndProductId(Long memberId, Long productId);

	@Query("""
		    select s from Subscription s
		    where s.member.nickname like %:keyword%
		       or s.product.name like %:keyword%
		""")
	List<Subscription> search(@Param("keyword") String keyword);
}
