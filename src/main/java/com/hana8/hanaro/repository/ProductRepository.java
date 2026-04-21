package com.hana8.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana8.hanaro.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
