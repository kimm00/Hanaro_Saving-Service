package com.hana8.hanaro.dto.account;

import com.hana8.hanaro.common.enums.AccountStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponseDTO {

	private Long id;
	private String accountNumber;
	private Long memberId;   // 중요 (Member 직접 주면 안됨)
	private Long balance;
	private Long paymentAmount;
	private Double interestRate;
	private AccountStatus status;
}
