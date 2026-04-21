package com.hana8.hanaro.dto.product;

import com.hana8.hanaro.common.enums.PaymentCycle;
import com.hana8.hanaro.common.enums.ProductType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductRequestDTO {

	@NotBlank(message = "상품명은 필수입니다.")
	private String name;

	@NotNull(message = "상품 타입은 필수입니다.")
	private ProductType productType;

	private PaymentCycle paymentCycle;

	@NotNull(message = "가입 기간은 필수입니다.")
	@Min(value = 1, message = "가입 기간은 1 이상이어야 합니다.")
	private Integer period;

	@NotNull(message = "이자율은 필수입니다.")
	@PositiveOrZero(message = "이자율은 0 이상이어야 합니다.")
	private Double interestRate;

	@NotNull(message = "해지 이율은 필수입니다.")
	@PositiveOrZero(message = "해지 이율은 0 이상이어야 합니다.")
	private Double cancelRate;

	@Min(value = 0, message = "납입 금액은 0 이상이어야 합니다.")
	private Long paymentAmount;
}
