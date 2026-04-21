package com.hana8.hanaro.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana8.hanaro.dto.member.MemberResponseDTO;
import com.hana8.hanaro.dto.member.RegisterRequestDTO;
import com.hana8.hanaro.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	// 회원가입
	@PostMapping("/register")
	public MemberResponseDTO registerMember(@Valid @RequestBody RegisterRequestDTO dto) {
		return memberService.register(
			dto.getEmail(),
			dto.getPassword(),
			dto.getNickname()
		);
	}

	// 회원 전체 조회 (관리자)
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	List<MemberResponseDTO> getMembers() {
		return memberService.getMembers();
	}
}
