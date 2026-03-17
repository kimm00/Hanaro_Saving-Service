package com.hana8.hanaro.service;

import java.util.List;

import com.hana8.hanaro.dto.product.ProductRequestDTO;
import com.hana8.hanaro.dto.product.ProductResponseDTO;

public interface ProductService {
	ProductResponseDTO createProduct(ProductRequestDTO dto);

	ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto);

	void deleteProduct(Long id);

	ProductResponseDTO getProduct(Long id);

	List<ProductResponseDTO> getProducts();

}
