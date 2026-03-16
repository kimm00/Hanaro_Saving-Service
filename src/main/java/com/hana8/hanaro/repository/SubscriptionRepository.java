package com.hana8.hanaro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana8.hanaro.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	List<Subscription> findByMemberId(Long memberId);

	// 중복 가입 방지
	boolean existsByMemberIdAndProductId(Long memberId, Long productId);
}
