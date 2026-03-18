package com.hana8.hanaro.dto.account;

import com.hana8.hanaro.common.enums.AccountStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponseDTO {

	private Long id;
	private String accountNumber;
	private Long memberId;
	private Long balance;
	private Long paymentAmount;
	private Double interestRate;
	private AccountStatus status;
	private Long productId;
	private Double cancelRate;

	private Long currentInterest;       // 지금까지 이자
	private Long expectedAmount;        // 만기 예상 금액
}
