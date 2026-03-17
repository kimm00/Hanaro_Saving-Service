package com.hana8.hanaro.service;

import java.util.List;

import com.hana8.hanaro.dto.subscription.SubscriptionRequestDTO;
import com.hana8.hanaro.dto.subscription.SubscriptionResponseDTO;

public interface SubscriptionService {

	SubscriptionResponseDTO subscribe(SubscriptionRequestDTO dto);

	List<SubscriptionResponseDTO> getSubscriptions(Long memberId);

	SubscriptionResponseDTO cancelSubscription(Long subscriptionId);
}
