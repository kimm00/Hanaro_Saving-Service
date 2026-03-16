package com.hana8.hanaro.entity;

import com.hana8.hanaro.common.enums.PaymentCycle;
import com.hana8.hanaro.common.enums.ProductType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 상품명
	@Column(nullable = false)
	private String name;

	// 예금 / 적금
	@Enumerated(EnumType.STRING)
	private ProductType productType;

	// 적금 납입주기
	@Enumerated(EnumType.STRING)
	private PaymentCycle paymentCycle;

	// 가입 기간 (개월)
	private Integer period;

	// 만기 수익률
	@Column(nullable = false)
	private Double interestRate;

	// 해지 수익률
	private Double cancelRate;

	// 대표 이미지
	private String imagePath;

}
