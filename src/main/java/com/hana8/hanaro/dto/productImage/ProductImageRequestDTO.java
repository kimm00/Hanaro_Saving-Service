package com.hana8.hanaro.dto.productImage;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProductImageRequestDTO {
	private Long productId;

	private MultipartFile files;

	private String remarks;
}
