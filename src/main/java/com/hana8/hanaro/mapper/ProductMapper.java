package com.hana8.hanaro.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hana8.hanaro.dto.product.ProductRequestDTO;
import com.hana8.hanaro.dto.product.ProductResponseDTO;
import com.hana8.hanaro.dto.productImage.ProductImageResponseDTO;
import com.hana8.hanaro.entity.Product;
import com.hana8.hanaro.entity.ProductImage;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	// Entity -> Response DTO
	@Mapping(target = "imageUrl", expression = "java(getThumbnail(product))")
	@Mapping(target = "images", expression = "java(getFirstImage(product))")
	ProductResponseDTO toDTO(Product product);

	// RequestDTO → Entity
	Product toEntity(ProductRequestDTO dto);

	// ProductImage -> ProductImageResponseDTO
	@Mapping(target = "thumbnail", source = "thumbnail")
	ProductImageResponseDTO toImageDTO(ProductImage image);

	default String getThumbnail(Product product) {
		if (product.getImages() == null || product.getImages().isEmpty()) {
			return null;
		}

		return product.getImages().stream()
			.filter(ProductImage::isThumbnail)
			.findFirst()
			.map(ProductImage::getImageUrl)
			.orElse(null);
	}

	default ProductImageResponseDTO getFirstImage(Product product) {
		if (product.getImages() == null || product.getImages().isEmpty())
			return null;

		ProductImage image = product.getImages().get(0);

		return ProductImageResponseDTO.builder()
			.id(image.getId())
			.imageUrl(image.getImageUrl())
			.remark(image.getRemark())
			.thumbnail(image.isThumbnail())
			.build();
	}
}
