package com.hana8.hanaro.repository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hana8.hanaro.entity.Product;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

	@Mock
	ProductRepository repository;

	@Test
	void findById() {
		when(repository.findById(1L))
			.thenReturn(Optional.of(new Product()));

		var result = repository.findById(1L);

		assertThat(result).isPresent();
	}

	@Test
	void findAll() {
		when(repository.findAll())
			.thenReturn(List.of(new Product()));

		var result = repository.findAll();

		assertThat(result).hasSize(1);
	}
}
