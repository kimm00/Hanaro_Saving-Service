package com.hana8.hanaro.service;

import java.util.List;

import com.hana8.hanaro.entity.Subscription;

public interface SubscriptionService {

	Subscription subscribe(Long memberId, Long productId, Long accountId);

	List<Subscription> getSubscriptions(Long memberId);

	Subscription cancelSubscription(Long subscriptionId);
}
