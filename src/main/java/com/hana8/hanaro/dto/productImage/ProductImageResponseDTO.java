package com.hana8.hanaro.dto.productImage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImageResponseDTO {

	private Long id;
	private String imageUrl;
	private String remark;
	private boolean thumbnail;
}
