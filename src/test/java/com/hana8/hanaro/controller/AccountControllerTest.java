package com.hana8.hanaro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.hana8.hanaro.dto.account.AccountResponseDTO;
import com.hana8.hanaro.service.AccountService;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

	@Mock
	private AccountService accountService;

	@InjectMocks
	private AccountController accountController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
	}

	@Test
	void createDefaultAccount() {
		Long memberId = 1L;

		AccountResponseDTO mockResponse = AccountResponseDTO.builder()
			.id(1L)
			.accountNumber("123-456-7890")
			.memberId(memberId)
			.build();

		when(accountService.createDefaultAccount(memberId))
			.thenReturn(mockResponse);

		AccountResponseDTO result = accountController.createDefaultAccount(memberId);

		verify(accountService).createDefaultAccount(memberId);
		assertEquals(memberId, result.getMemberId());
		assertNotNull(result);
	}

	@Test
	void createDefaultAccount_fail() {
		Long memberId = 1L;

		when(accountService.createDefaultAccount(memberId))
			.thenThrow(new IllegalArgumentException());

		assertThrows(IllegalArgumentException.class,
			() -> accountController.createDefaultAccount(memberId));

		verify(accountService).createDefaultAccount(memberId);
	}

	@Test
	void getAccountsByMember() {
		Long memberId = 1L;

		List<AccountResponseDTO> mockList = List.of(
			AccountResponseDTO.builder().id(1L).memberId(memberId).build()
		);

		when(accountService.getAccounts(memberId)).thenReturn(mockList);

		List<AccountResponseDTO> result = accountController.getAccountsByMember(memberId);

		verify(accountService).getAccounts(memberId);
		assertEquals(1, result.size());
	}

	@Test
	void getAccountsByMember_fail() {
		Long memberId = 1L;

		when(accountService.getAccounts(memberId))
			.thenThrow(new RuntimeException());

		assertThrows(RuntimeException.class,
			() -> accountController.getAccountsByMember(memberId));

		verify(accountService).getAccounts(memberId);
	}

	@Test
	void getAccountDetail() {
		Long accountId = 1L;

		AccountResponseDTO mockResponse = AccountResponseDTO.builder()
			.id(accountId)
			.build();

		when(accountService.previewAccount(accountId)).thenReturn(mockResponse);

		AccountResponseDTO result = accountController.getAccountDetail(accountId);

		verify(accountService).previewAccount(accountId);
		assertEquals(accountId, result.getId());
	}

	@Test
	void getAccountDetail_fail() {
		Long accountId = 1L;

		when(accountService.previewAccount(accountId))
			.thenThrow(new IllegalArgumentException());

		assertThrows(IllegalArgumentException.class,
			() -> accountController.getAccountDetail(accountId));

		verify(accountService).previewAccount(accountId);
	}

	@Test
	void makePayment() {
		Long accountId = 1L;

		AccountResponseDTO mockResponse = AccountResponseDTO.builder()
			.id(accountId)
			.build();

		when(accountService.makePayment(accountId)).thenReturn(mockResponse);

		AccountResponseDTO result = accountController.makePayment(accountId);

		verify(accountService).makePayment(accountId);
		assertEquals(accountId, result.getId());
	}

	@Test
	void makePayment_fail() {
		Long accountId = 1L;

		when(accountService.makePayment(accountId))
			.thenThrow(new IllegalArgumentException());

		assertThrows(IllegalArgumentException.class,
			() -> accountController.makePayment(accountId));

		verify(accountService).makePayment(accountId);
	}

	@Test
	void cancelAccount() {
		Long accountId = 1L;

		AccountResponseDTO mockResponse = AccountResponseDTO.builder()
			.id(accountId)
			.build();

		when(accountService.cancelAccount(accountId)).thenReturn(mockResponse);

		AccountResponseDTO result = accountController.cancelAccount(accountId);

		verify(accountService).cancelAccount(accountId);
		assertEquals(accountId, result.getId());
	}

	@Test
	void cancelAccount_fail() {
		Long accountId = 1L;

		when(accountService.cancelAccount(accountId))
			.thenThrow(new IllegalArgumentException());

		assertThrows(IllegalArgumentException.class,
			() -> accountController.cancelAccount(accountId));

		verify(accountService).cancelAccount(accountId);
	}

	@Test
	void completeAccount() {
		Long accountId = 1L;

		AccountResponseDTO mockResponse = AccountResponseDTO.builder()
			.id(accountId)
			.build();

		when(accountService.completeAccount(accountId)).thenReturn(mockResponse);

		AccountResponseDTO result = accountController.completeAccount(accountId);

		verify(accountService).completeAccount(accountId);
		assertEquals(accountId, result.getId());
	}

	@Test
	void completeAccount_fail() {
		Long accountId = 1L;

		when(accountService.completeAccount(accountId))
			.thenThrow(new IllegalArgumentException());

		assertThrows(IllegalArgumentException.class,
			() -> accountController.completeAccount(accountId));

		verify(accountService).completeAccount(accountId);
	}

	@Test
	void completeAccount_mockmvc() throws Exception {
		Long accountId = 1L;

		AccountResponseDTO mockResponse = AccountResponseDTO.builder()
			.id(accountId)
			.build();

		when(accountService.completeAccount(accountId)).thenReturn(mockResponse);

		mockMvc.perform(
<<<<<<< HEAD
				patch("/api/accounts/admin/{accountId}/complete", accountId) // 🔥 FIX
=======
				patch("/api/accounts/admin/accounts/{accountId}/complete", accountId)
>>>>>>> b70aeda18ad66a4c0fc4e7455f9f211b5c41e237
			)
			.andExpect(status().isOk());

		verify(accountService).completeAccount(accountId);
	}

	@Test
	void createDefaultAccount_mockmvc() throws Exception {
		Long memberId = 1L;

		AccountResponseDTO mockResponse = AccountResponseDTO.builder()
			.id(1L)
			.memberId(memberId)
			.build();

		when(accountService.createDefaultAccount(memberId)).thenReturn(mockResponse);

		mockMvc.perform(
				post("/api/accounts/default-account")
					.param("memberId", String.valueOf(memberId))
			)
			.andExpect(status().isOk());

		verify(accountService).createDefaultAccount(memberId);
	}
}
