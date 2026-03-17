package com.hana8.hanaro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana8.hanaro.dto.product.ProductRequestDTO;
import com.hana8.hanaro.dto.product.ProductResponseDTO;
import com.hana8.hanaro.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;

	@PostMapping
	ProductResponseDTO createProduct(@RequestBody ProductRequestDTO dto) {
		return productService.createProduct(dto);
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
	ProductResponseDTO updateProduct(
		@PathVariable Long id,
		@RequestBody ProductRequestDTO dto) {
		return productService.updateProduct(id, dto);
	}

	@DeleteMapping("/{id}")
	public void deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
	}
}
