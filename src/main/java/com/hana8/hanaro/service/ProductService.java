package com.hana8.hanaro.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hana8.hanaro.dto.product.ProductRequestDTO;
import com.hana8.hanaro.dto.product.ProductResponseDTO;
import com.hana8.hanaro.dto.productImage.ProductImageRequestDTO;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.ProductImage;
import com.hana8.hanaro.mapper.ProductMapper;
import com.hana8.hanaro.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	@Value("${upload.path}")
	private String uploadBasePath;

	private String getTodayFolder() {
		return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}

	// 생성
	public ProductResponseDTO createProduct(ProductRequestDTO dto) {
		log.info("상품 생성: name={}", dto.getName());

		Product product = productMapper.toEntity(dto);
		return productMapper.toDTO(productRepository.save(product));
	}

	// 수정
	public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
		log.info("상품 수정: id={}, name={}", id, dto.getName());

		Product product = productRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

		// DTO → Entity 값 업데이트
		product.setName(dto.getName());
		product.setProductType(dto.getProductType());
		product.setPaymentCycle(dto.getPaymentCycle());
		product.setPeriod(dto.getPeriod());
		product.setInterestRate(dto.getInterestRate());
		product.setCancelRate(dto.getCancelRate());

		return productMapper.toDTO(productRepository.save(product));
	}

	// 삭제
	public void deleteProduct(Long id) {
		log.info("상품 삭제: id={}", id);

		Product product = productRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

		productRepository.delete(product);
	}

	// 이미지 업로드
	public ProductResponseDTO uploadImages(ProductImageRequestDTO dto) {
		System.out.println("=== 이미지 업로드 시작 ===");

		Product product = productRepository.findById(dto.getProductId())
			.orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

		MultipartFile file = dto.getFiles();
		String remark = dto.getRemarks();

		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("파일이 비어있습니다.");
		}

		if (file.getSize() > 2 * 1024 * 1024) {
			throw new IllegalArgumentException("파일 최대 크기는 2MB입니다.");
		}

		String today = getTodayFolder();
		Path uploadPath = Paths.get(uploadBasePath, today);

		try {
			Files.createDirectories(uploadPath);
		} catch (IOException e) {
			throw new IllegalArgumentException("폴더 생성 실패");
		}

		String orgName = file.getOriginalFilename();
		String saveName = UUID.randomUUID() + "_" + orgName;

		try {
			Path filePath = uploadPath.resolve(saveName);

			file.transferTo(filePath.toFile());

		} catch (IOException e) {
			throw new IllegalArgumentException("파일 저장 실패");
		}

		boolean hasThumbnail = product.getImages() != null &&
			product.getImages().stream().anyMatch(ProductImage::isThumbnail);

		boolean isThumbnail = !hasThumbnail;

		ProductImage image = ProductImage.builder()
			.orgName(orgName)
			.saveName(saveName)
			.saveDir(today)
			.remark(remark)
			.isThumbnail(isThumbnail)
			.build();

		product.addImage(image);

		System.out.printf("이미지 엔티티 추가 완료");

		Product saved = productRepository.save(product);

		return productMapper.toDTO(saved);
	}

	// 이미지 삭제
	public void deleteImage(Long productId, Long imageId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

		ProductImage target = product.getImages().stream()
			.filter(img -> img.getId().equals(imageId))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("이미지가 존재하지 않습니다."));

		boolean wasThumbnail = target.isThumbnail();

		Path filePath = Paths.get(uploadBasePath, target.getSaveDir(), target.getSaveName());

		try {
			Files.deleteIfExists(filePath);
		} catch (IOException e) {
			throw new IllegalArgumentException("파일 삭제를 실패했습니다.");
		}

		product.getImages().remove(target);

		if (wasThumbnail && !product.getImages().isEmpty()) {
			product.getImages().get(0).setThumbnail(true);
		}

		productRepository.save(product);
	}

	// 단건 조회
	public ProductResponseDTO getProduct(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

		return productMapper.toDTO(product);
	}

	// 전체 조회
	public List<ProductResponseDTO> getProducts() {
		return productRepository.findAll()
			.stream()
			.map(productMapper::toDTO)
			.toList();
	}

}
