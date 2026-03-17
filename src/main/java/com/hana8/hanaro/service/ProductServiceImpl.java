package com.hana8.hanaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hana8.hanaro.dto.product.ProductRequestDTO;
import com.hana8.hanaro.dto.product.ProductResponseDTO;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.mapper.ProductMapper;
import com.hana8.hanaro.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	// 생성
	@Override
	public ProductResponseDTO createProduct(ProductRequestDTO dto) {
		Product product = productMapper.toEntity(dto);
		return productMapper.toDTO(productRepository.save(product));
	}

	// 수정
	@Override
	public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

		// DTO → Entity 값 업데이트
		product.setName(dto.getName());
		product.setProductType(dto.getProductType());
		product.setPaymentCycle(dto.getPaymentCycle());
		product.setPeriod(dto.getPeriod());
		product.setInterestRate(dto.getInterestRate());
		product.setCancelRate(dto.getCancelRate());
		product.setImagePath(dto.getImagePath());

		return productMapper.toDTO(productRepository.save(product));
	}

	// 삭제
	@Override
	public void deleteProduct(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

		productRepository.delete(product);
	}

	// 단건 조회
	@Override
	public ProductResponseDTO getProduct(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

		return productMapper.toDTO(product);
	}

	// 전체 조회
	@Override
	public List<ProductResponseDTO> getProducts() {
		return productRepository.findAll()
			.stream()
			.map(productMapper::toDTO)
			.toList();
	}

}
