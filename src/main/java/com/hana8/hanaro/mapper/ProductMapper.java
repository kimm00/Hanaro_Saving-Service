package com.hana8.hanaro.mapper;

import org.mapstruct.Mapper;

import com.hana8.hanaro.dto.product.ProductRequestDTO;
import com.hana8.hanaro.dto.product.ProductResponseDTO;
import com.hana8.hanaro.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	// Entity -> Response DTO
	ProductResponseDTO toDTO(Product product);

	// RequestDTO → Entity
	Product toEntity(ProductRequestDTO dto);
}
