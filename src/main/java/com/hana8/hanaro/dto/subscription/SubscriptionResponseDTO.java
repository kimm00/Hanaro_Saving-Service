package com.hana8.hanaro.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionResponseDTO {
	private Long id;

	private String memberName;
	private Long memberId;
	private Long productId;
	private String productName;

	private Long accountId;

	private Long paymentAmount;
	private Double interestRate;
	private Integer period;

	private boolean canceled;

	private Long currentInterest;
}
