package com.hana8.hanaro.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana8.hanaro.dto.product.ProductRequestDTO;
import com.hana8.hanaro.dto.product.ProductResponseDTO;
import com.hana8.hanaro.dto.productImage.ProductImageRequestDTO;
import com.hana8.hanaro.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	ProductResponseDTO createProduct(@RequestBody ProductRequestDTO dto) {
		return productService.createProduct(dto);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(
		value = "/{productId}/images",
		consumes = "multipart/form-data"
	)
	public ProductResponseDTO uploadImages(
		@PathVariable Long productId,
		@ModelAttribute ProductImageRequestDTO dto
	) {
		dto.setProductId(productId);
		return productService.uploadImages(dto);
	}

	@GetMapping
	List<ProductResponseDTO> getProducts() {

		return productService.getProducts();
	}

	@GetMapping("/{id}")
	ProductResponseDTO getProduct(@PathVariable Long id) {

		return productService.getProduct(id);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	ProductResponseDTO updateProduct(
		@PathVariable("id") Long id,
		@RequestBody ProductRequestDTO dto) {
		return productService.updateProduct(id, dto);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteProduct(@PathVariable("id") Long id) {
		productService.deleteProduct(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{productId}/images/{imageId}")
	public void deleteProductImage(
		@PathVariable("productId") Long productId,
		@PathVariable("imageId") Long imageId
	) {
		productService.deleteImage(productId, imageId);
	}

}
