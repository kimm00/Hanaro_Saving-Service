package com.hana8.hanaro.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hana8.hanaro.dto.member.MemberResponseDTO;
import com.hana8.hanaro.dto.member.RegisterRequestDTO;
import com.hana8.hanaro.service.MemberService;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

	@Mock
	private MemberService memberService;

	@InjectMocks
	private MemberController memberController;

	@Test
	void registerMember() {
		RegisterRequestDTO dto = new RegisterRequestDTO();
		dto.setEmail("test@test.com");
		dto.setPassword("12345678");
		dto.setNickname("test");

		MemberResponseDTO res = MemberResponseDTO.builder()
			.id(1L)
			.email("test@test.com")
			.build();

		when(memberService.register(any(), any(), any()))
			.thenReturn(res);

		MemberResponseDTO result = memberController.registerMember(dto);

		assertEquals(1L, result.getId());
		verify(memberService).register(any(), any(), any());
	}

	@Test
	void getMembers() {
		when(memberService.getMembers()).thenReturn(List.of());

		List<MemberResponseDTO> result = memberController.getMembers();

		assertNotNull(result);
		verify(memberService).getMembers();
	}
}
