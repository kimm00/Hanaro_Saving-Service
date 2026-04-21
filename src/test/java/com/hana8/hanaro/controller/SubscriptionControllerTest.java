package com.hana8.hanaro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.hana8.hanaro.dto.subscription.SubscriptionRequestDTO;
import com.hana8.hanaro.dto.subscription.SubscriptionResponseDTO;
import com.hana8.hanaro.service.SubscriptionService;

@ExtendWith(MockitoExtension.class)
class SubscriptionControllerTest {

	@Mock
	private SubscriptionService subscriptionService;

	@InjectMocks
	private SubscriptionController subscriptionController;

	@Test
	void subscribeProduct() {
<<<<<<< HEAD
		// given
		SubscriptionRequestDTO dto = new SubscriptionRequestDTO();

		Authentication auth = mock(Authentication.class);
		when(auth.getName()).thenReturn("test@email.com");

		when(subscriptionService.subscribe(eq(dto), anyString()))
			.thenReturn(SubscriptionResponseDTO.builder().id(1L).build());

		// when
		SubscriptionResponseDTO result =
			subscriptionController.subscribeProduct(dto, auth);

		// then
=======
		SubscriptionRequestDTO dto = new SubscriptionRequestDTO();

		when(subscriptionService.subscribe(dto))
			.thenReturn(SubscriptionResponseDTO.builder().id(1L).build());

		SubscriptionResponseDTO result = subscriptionController.subscribeProduct(dto);

>>>>>>> b70aeda18ad66a4c0fc4e7455f9f211b5c41e237
		assertEquals(1L, result.getId());
	}

	@Test
	void getMySubscriptions() {
		Authentication auth = mock(Authentication.class);
		when(auth.getName()).thenReturn("test@test.com");

		when(subscriptionService.getMySubscriptions("test@test.com"))
			.thenReturn(List.of());

		List<SubscriptionResponseDTO> result =
			subscriptionController.getMySubscriptions(auth);

		assertNotNull(result);
	}

	@Test
	void cancelSubscription() {
<<<<<<< HEAD
		Authentication auth = mock(Authentication.class);
		when(auth.getName()).thenReturn("test@test.com");

		when(subscriptionService.cancelSubscription(1L, "test@test.com"))
			.thenReturn(SubscriptionResponseDTO.builder().id(1L).build());

		SubscriptionResponseDTO result =
			subscriptionController.cancelSubscription(1L, auth);
=======
		when(subscriptionService.cancelSubscription(1L))
			.thenReturn(SubscriptionResponseDTO.builder().id(1L).build());

		SubscriptionResponseDTO result =
			subscriptionController.cancelSubscription(1L);
>>>>>>> b70aeda18ad66a4c0fc4e7455f9f211b5c41e237

		assertEquals(1L, result.getId());
	}

	@Test
	void adminSearch() {
		when(subscriptionService.adminSearch(null, null))
			.thenReturn(List.of());

		List<SubscriptionResponseDTO> result =
			subscriptionController.adminSearch(null, null);

		assertNotNull(result);
	}

	@Test
	void getMemberSubscriptions() {
		when(subscriptionService.getSubscriptions(1L))
			.thenReturn(List.of());

		List<SubscriptionResponseDTO> result =
			subscriptionController.getMemberSubscriptions(1L);

		assertNotNull(result);
	}

	@Test
	void matureSubscription() {
		when(subscriptionService.matureSubscription(1L))
			.thenReturn(SubscriptionResponseDTO.builder().id(1L).build());

		SubscriptionResponseDTO result =
			subscriptionController.matureSubscription(1L);

		assertEquals(1L, result.getId());
	}
}
