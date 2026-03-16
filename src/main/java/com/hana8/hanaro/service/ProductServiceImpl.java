package com.hana8.hanaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;

	@Override
	public Product createProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Product updateProduct(Long id, Product product) {
		Product p = productRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

		p.setName(product.getName());
		p.setProductType(product.getProductType());
		p.setPaymentCycle(product.getPaymentCycle());
		p.setPeriod(product.getPeriod());
		p.setInterestRate(product.getInterestRate());
		p.setCancelRate(product.getCancelRate());
		p.setImagePath(product.getImagePath());

		return productRepository.save(p);
	}

	@Override
	public void deleteProduct(Long id) {
		Product p = productRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

		productRepository.delete(p);
	}

	@Override
	public Product getProduct(Long id) {
		return productRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));
	}

	@Override
	public List<Product> getProducts() {
		return productRepository.findAll();
	}

}
