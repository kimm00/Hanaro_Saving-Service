package com.hana8.hanaro.dto.productImage;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ProductImageRequestDTO {
	private Long productId;
	@NotEmpty
	private List<MultipartFile> files;

	private List<String> remarks;
}
