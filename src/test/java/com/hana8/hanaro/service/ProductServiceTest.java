package com.hana8.hanaro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.hana8.hanaro.dto.product.ProductRequestDTO;
import com.hana8.hanaro.dto.product.ProductResponseDTO;
import com.hana8.hanaro.dto.productImage.ProductImageRequestDTO;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.mapper.ProductMapper;
import com.hana8.hanaro.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;
	@Mock
	private ProductMapper productMapper;

	@InjectMocks
	private ProductService productService;

	@Test
	void createProduct() {
		ProductRequestDTO dto = mock(ProductRequestDTO.class);
		productService.createProduct(dto);
		verify(productMapper).toEntity(dto);
	}

	@Test
	void createProduct_success() {

		ProductRequestDTO dto = new ProductRequestDTO();

		when(productMapper.toEntity(dto)).thenReturn(new Product());
		when(productRepository.save(any())).thenReturn(new Product());
		when(productMapper.toDTO(any())).thenReturn(mock(ProductResponseDTO.class));

		productService.createProduct(dto);

		verify(productRepository).save(any());
	}

	@Test
	void updateProduct() {
		ProductRequestDTO dto = mock(ProductRequestDTO.class);
		assertThrows(Exception.class,
			() -> productService.updateProduct(1L, dto));
	}

	@Test
	void updateProduct_success() {

		Product product = new Product();

		when(productRepository.findById(1L))
			.thenReturn(Optional.of(product));

		when(productRepository.save(any()))
			.thenReturn(product);

		when(productMapper.toDTO(any()))
			.thenReturn(mock(ProductResponseDTO.class));

		ProductRequestDTO dto = new ProductRequestDTO();
		dto.setName("테스트");

		productService.updateProduct(1L, dto);

		verify(productRepository).save(any());
	}

	@Test
	void deleteProduct() {
		assertThrows(Exception.class,
			() -> productService.deleteProduct(1L));
	}

	@Test
	void deleteProduct_success() {

		when(productRepository.findById(1L))
			.thenReturn(Optional.of(new Product()));

		productService.deleteProduct(1L);

		verify(productRepository).delete(any());
	}

	@Test
	void uploadImages() {
		ProductImageRequestDTO dto = mock(ProductImageRequestDTO.class);
		assertThrows(Exception.class,
			() -> productService.uploadImages(dto));
	}

	@Test
	void deleteImage() {
		assertThrows(Exception.class,
			() -> productService.deleteImage(1L, 1L));
	}

	@Test
	void getProduct() {
		assertThrows(Exception.class,
			() -> productService.getProduct(1L));
	}

	@Test
	void getProduct_success() {

		when(productRepository.findById(1L))
			.thenReturn(Optional.of(new Product()));

		when(productMapper.toDTO(any()))
			.thenReturn(mock(ProductResponseDTO.class));

		productService.getProduct(1L);

		verify(productRepository).findById(1L);
	}

	@Test
	void getProducts() {
		productService.getProducts();
		verify(productRepository).findAll();
	}

	@Test
	void getProducts_success() {

		when(productRepository.findAll())
			.thenReturn(List.of(new Product()));

		when(productMapper.toDTO(any()))
			.thenReturn(mock(ProductResponseDTO.class));

		productService.getProducts();

		verify(productRepository).findAll();
	}

	@Test
	void uploadImages_fail_emptyFile() {

		MultipartFile file = mock(MultipartFile.class);

		when(file.isEmpty()).thenReturn(true);

		ProductImageRequestDTO dto = new ProductImageRequestDTO();
		dto.setProductId(1L);
		dto.setFiles(file);

		when(productRepository.findById(1L))
			.thenReturn(Optional.of(new Product()));

		assertThrows(IllegalArgumentException.class,
			() -> productService.uploadImages(dto));
	}

	@Test
	void uploadImages_fail_sizeOver() {

		MultipartFile file = mock(MultipartFile.class);

		when(file.isEmpty()).thenReturn(false);
		when(file.getSize()).thenReturn(3 * 1024 * 1024L); // 3MB

		ProductImageRequestDTO dto = new ProductImageRequestDTO();
		dto.setProductId(1L);
		dto.setFiles(file);

		when(productRepository.findById(1L))
			.thenReturn(Optional.of(new Product()));

		assertThrows(IllegalArgumentException.class,
			() -> productService.uploadImages(dto));
	}

	@Test
	void deleteImage_fail_notFound() {

		Product product = new Product();
		product.setImages(new ArrayList<>());

		when(productRepository.findById(1L))
			.thenReturn(Optional.of(product));

		assertThrows(IllegalArgumentException.class,
			() -> productService.deleteImage(1L, 1L));
	}
}
