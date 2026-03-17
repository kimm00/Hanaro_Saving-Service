package com.hana8.hanaro.dto.subscription;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionResponseDTO {
	private Long id;

	private Long memberId;
	private String memberName;

	private Long productId;
	private String productName;

	private Long accountId;

	private Long paymentAmount;
	private Double interestRate;
	private Integer period;

	private boolean canceled;

	private Long currentInterest;
}
