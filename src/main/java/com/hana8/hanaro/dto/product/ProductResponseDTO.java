package com.hana8.hanaro.dto.product;

import com.hana8.hanaro.common.enums.PaymentCycle;
import com.hana8.hanaro.common.enums.ProductType;
import com.hana8.hanaro.dto.productImage.ProductImageResponseDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDTO {

	private Long id;
	private String name;
	private ProductType productType;
	private PaymentCycle paymentCycle;
	private Integer period;
	private Double interestRate;
	private Double cancelRate;
	private String imageUrl;

	private ProductImageResponseDTO images;
}
