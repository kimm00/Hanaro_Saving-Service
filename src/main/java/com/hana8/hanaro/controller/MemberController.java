package com.hana8.hanaro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana8.hanaro.dto.member.LoginRequestDTO;
import com.hana8.hanaro.dto.member.MemberResponseDTO;
import com.hana8.hanaro.dto.member.RegisterRequestDTO;
import com.hana8.hanaro.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	// 회원가입
	@PostMapping("/register")
	public MemberResponseDTO registerMember(@RequestBody RegisterRequestDTO dto) {
		return memberService.register(
			dto.getEmail(),
			dto.getPassword(),
			dto.getNickname()
		);
	}

	// 로그인
	@PostMapping("/login")
	public MemberResponseDTO loginMember(@RequestBody LoginRequestDTO dto) {
		return memberService.login(
			dto.getEmail(),
			dto.getPassword()
		);
	}

	// 회원 전체 조회 (관리자)
	@GetMapping
	List<MemberResponseDTO> getMembers() {
		return memberService.getMembers();
	}

	// 회원 상세 조회
	@GetMapping("/{id}")
	MemberResponseDTO getMember(@PathVariable Long id) {
		return memberService.getMember(id);
	}
}
