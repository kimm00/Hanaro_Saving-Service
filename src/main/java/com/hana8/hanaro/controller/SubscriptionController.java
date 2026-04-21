package com.hana8.hanaro.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hana8.hanaro.dto.subscription.SubscriptionRequestDTO;
import com.hana8.hanaro.dto.subscription.SubscriptionResponseDTO;
import com.hana8.hanaro.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SubscriptionController {
	private final SubscriptionService subscriptionService;

	// --- 사용자(User) 기능 ---

	// 상품 가입
	@PostMapping("/subscriptions")
	public SubscriptionResponseDTO subscribeProduct(
		SubscriptionRequestDTO dto,
		Authentication auth
	) {
		return subscriptionService.subscribe(dto, auth.getName());
	}

	// 내 가입 상품 조회 (JWT 기반)
	@GetMapping("/subscriptions/my")
	public List<SubscriptionResponseDTO> getMySubscriptions(Authentication authentication) {
		String email = authentication.getName();

		return subscriptionService.getMySubscriptions(email);
	}

	// 중도 해지
	@PostMapping("/subscriptions/{subscriptionId}/cancel")
	public SubscriptionResponseDTO cancelSubscription(
		@PathVariable Long subscriptionId,
		Authentication authentication
	) {
		String email = authentication.getName();

		return subscriptionService.cancelSubscription(subscriptionId, email);
	}

	// --- 관리자(Admin) 기능 ---

	// 전체 가입 내역 조회 (검색 포함)
	@GetMapping("/admin/subscriptions")
	@PreAuthorize("hasRole('ADMIN')")
	public List<SubscriptionResponseDTO> adminSearch(
		@RequestParam(value = "memberId", required = false) Long memberId,
		@RequestParam(value = "status", required = false) String status
	) {
		return subscriptionService.adminSearch(memberId, status);
	}

	// 특정 회원의 가입 상품 조회
	@GetMapping("/admin/members/{memberId}/subscriptions")
	@PreAuthorize("hasRole('ADMIN')")
	public List<SubscriptionResponseDTO> getMemberSubscriptions(
		@PathVariable("memberId") Long memberId
	) {
		return subscriptionService.getSubscriptions(memberId);
	}

	// 가입 상품 만기 처리
	@PostMapping("/admin/subscriptions/{subscriptionId}/complete")
	@PreAuthorize("hasRole('ADMIN')")
	public SubscriptionResponseDTO matureSubscription(
		@PathVariable("subscriptionId") Long subscriptionId
	) {
		return subscriptionService.matureSubscription(subscriptionId);
	}

}
