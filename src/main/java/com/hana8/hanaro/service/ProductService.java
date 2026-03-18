package com.hana8.hanaro.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	private String getTodayFolder() {
		return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}

	// 생성
	public ProductResponseDTO createProduct(ProductRequestDTO dto) {
		Product product = productMapper.toEntity(dto);
		return productMapper.toDTO(productRepository.save(product));
	}

	// 수정
	public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
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
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

		productRepository.delete(product);
	}

	// 이미지 업로드
	public ProductResponseDTO uploadImages(ProductImageRequestDTO dto) {

		Product product = productRepository.findById(dto.getProductId())
			.orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

		List<MultipartFile> files = dto.getFiles();
		List<String> remarks = dto.getRemarks();

		// 날짜 폴더
		String today = getTodayFolder();
		Path uploadPath = Paths.get("src/main/resources/static/upload/" + today);

		try {
			Files.createDirectories(uploadPath);
		} catch (IOException e) {
			throw new IllegalArgumentException("폴더 생성에 실패했습니다.");
		}

		long totalSize = 0;

		for (int i = 0; i < files.size(); i++) {
			MultipartFile file = files.get(i);

			// 파일 크기 체크
			if (file.getSize() > 2 * 1024 * 1024) {
				throw new IllegalArgumentException("파일 하나 최대 크기는 2MB입니다.");
			}

			totalSize += file.getSize();
			if (totalSize > 10 * 1024 * 1024) {
				throw new IllegalArgumentException("총 파일 크기는 10MB 초과할 수 없습니다.");
			}

			String orgName = file.getOriginalFilename();
			String saveName = UUID.randomUUID() + "_" + orgName; // 파일 이름 겹침 방지

			try {
				Path filePath = uploadPath.resolve(saveName);
				Files.write(filePath, file.getBytes());
			} catch (IOException e) {
				throw new IllegalArgumentException("파일 저장에 실패했습니다.");
			}

			String remark = (remarks != null && remarks.size() > i)
				? remarks.get(i)
				: null;

			boolean hasThumbnail = product.getImages().stream() // 기존 썸네일 있으면 새로 안 만듦
				.anyMatch(ProductImage::isThumbnail);

			boolean isThumbnail = (!hasThumbnail && i == 0); // 없으면 첫 번째 사진 썸네일

			ProductImage image = ProductImage.builder()
				.orgName(orgName)
				.saveName(saveName)
				.saveDir(today)
				.remark(remark)
				.isThumbnail(isThumbnail)
				.build();

			product.addImage(image);
		}

		return productMapper.toDTO(productRepository.save(product));
	}

	// 이미지 삭제
	public void deleteImage(Long productId, Long imageId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

		ProductImage target = product.getImages().stream()
			.filter(img -> img.getId().equals(imageId))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("이미지가 존재하지 않습니다."));

		if (target.isThumbnail()) {
			product.getImages().remove(target);

			// 남은 것 중 하나를 대표로
			if (!product.getImages().isEmpty()) {
				product.getImages().get(0).setThumbnail(true);
			}
		} else {
			product.getImages().remove(target);
		}
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
