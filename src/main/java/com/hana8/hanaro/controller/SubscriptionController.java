package com.hana8.hanaro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hana8.hanaro.dto.subscription.SubscriptionRequestDTO;
import com.hana8.hanaro.dto.subscription.SubscriptionResponseDTO;
import com.hana8.hanaro.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
	private final SubscriptionService subscriptionService;

	// 상품 가입
	@PostMapping
	public SubscriptionResponseDTO subscribeProduct(@RequestBody SubscriptionRequestDTO dto) {
		return subscriptionService.subscribe(dto);
	}

	// 관리자용 가입 상품 검색
	@GetMapping("/admin/search")
	public List<SubscriptionResponseDTO> adminSearch(
		@RequestParam String keyword
	) {
		return subscriptionService.searchSubscriptions(keyword);
	}

	// 내 가입 상품 조회
	@GetMapping("/member/{memberId}")
	public List<SubscriptionResponseDTO> getSubscriptions(@PathVariable Long memberId) {
		return subscriptionService.getSubscriptions(memberId);
	}

	// 가입 상품 검색
	@GetMapping("/search")
	public List<SubscriptionResponseDTO> searchSubscriptions(
		@RequestParam String keyword
	) {
		return subscriptionService.searchSubscriptions(keyword);
	}

	// 중도 해지
	@PostMapping("/{subscriptionId}/cancel")
	public SubscriptionResponseDTO cancelSubscription(@PathVariable Long subscriptionId) {
		return subscriptionService.cancelSubscription(subscriptionId);
	}
}
