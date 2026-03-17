package com.hana8.hanaro.dto.product;

import com.hana8.hanaro.common.enums.PaymentCycle;
import com.hana8.hanaro.common.enums.ProductType;

import lombok.Data;

@Data
public class ProductRequestDTO {

	private String name;
	private ProductType productType;
	private PaymentCycle paymentCycle;
	private Integer period;
	private Double interestRate;
	private Double cancelRate;
	private String imagePath;
}
