package com.hana8.hanaro.service;

import java.util.List;

import com.hana8.hanaro.entity.Product;

public interface ProductService {
	Product createProduct(Product product);

	Product updateProduct(Long id, Product product);

	void deleteProduct(Long id);

	Product getProduct(Long id);

	List<Product> getProducts();
}
