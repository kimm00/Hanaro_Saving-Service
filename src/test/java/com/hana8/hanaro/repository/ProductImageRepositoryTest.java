package com.hana8.hanaro.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hana8.hanaro.entity.ProductImage;

@ExtendWith(MockitoExtension.class)
class ProductImageRepositoryTest {

	@Mock
	ProductImageRepository repository;

	@Test
	void save() {
		ProductImage image = new ProductImage();

		when(repository.save(any()))
			.thenReturn(image);

		ProductImage result = repository.save(new ProductImage());

		assertThat(result).isNotNull();
	}
}
