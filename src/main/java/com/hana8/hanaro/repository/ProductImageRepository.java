package com.hana8.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana8.hanaro.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
