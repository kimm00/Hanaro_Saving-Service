package com.hana8.hanaro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hana8.hanaro.dto.product.ProductRequestDTO;
import com.hana8.hanaro.dto.product.ProductResponseDTO;
import com.hana8.hanaro.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

	@Mock
	private ProductService productService;

	@InjectMocks
	private ProductController productController;

	@Test
	void createProduct() {
		ProductRequestDTO dto = new ProductRequestDTO();
		dto.setName("상품");

		when(productService.createProduct(dto))
			.thenReturn(ProductResponseDTO.builder().id(1L).build());

		ProductResponseDTO result = productController.createProduct(dto);

		assertEquals(1L, result.getId());
		verify(productService).createProduct(dto);
	}

	@Test
	void getProducts() {
		when(productService.getProducts()).thenReturn(List.of());

		List<ProductResponseDTO> result = productController.getProducts();

		assertNotNull(result);
		verify(productService).getProducts();
	}

	@Test
	void getProduct() {
		when(productService.getProduct(1L))
			.thenReturn(ProductResponseDTO.builder().id(1L).build());

		ProductResponseDTO result = productController.getProduct(1L);

		assertEquals(1L, result.getId());
	}

	@Test
	void updateProduct() {
		ProductRequestDTO dto = new ProductRequestDTO();

		when(productService.updateProduct(eq(1L), any()))
			.thenReturn(ProductResponseDTO.builder().id(1L).build());

		ProductResponseDTO result = productController.updateProduct(1L, dto);

		assertEquals(1L, result.getId());
	}

	@Test
	void deleteProduct() {
		productController.deleteProduct(1L);

		verify(productService).deleteProduct(1L);
	}
}
