package com.hana8.hanaro.dto.subscription;

import lombok.Data;

@Data
public class SubscriptionRequestDTO {
	private Long memberId;
	private Long productId;
	private Long accountId;
}
