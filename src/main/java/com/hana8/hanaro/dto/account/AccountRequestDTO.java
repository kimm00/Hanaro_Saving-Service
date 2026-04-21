package com.hana8.hanaro.dto.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountRequestDTO {
	@NotNull
	private Long memberId;

	@NotNull
	private Long productId;

	@NotNull
	@Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}",
		message = "계좌번호는 000-0000-0000 형식이어야 합니다.")
	private String accountNumber;
}
