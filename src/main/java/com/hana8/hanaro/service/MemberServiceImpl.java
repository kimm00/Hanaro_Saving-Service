package com.hana8.hanaro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hana8.hanaro.common.enums.Role;
import com.hana8.hanaro.dto.member.MemberResponseDTO;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final AccountService accountService;

	@Override
	public Member register(String email, String password, String nickname) {
		if (memberRepository.existsByEmail(email)) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}

		Member member = Member.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.role(Role.USER)
			.build();

		member = memberRepository.save(member);

		// 회원가입 시 자유입출금 계좌 자동 생성
		accountService.createDefaultAccount(member.getId());

		return member;
	}

	@Override
	public Member login(String email, String password) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

		if (!member.getPassword().equals(password)) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}

		return member;
	}

	@Override
	public List<MemberResponseDTO> getMembers() {
		return memberRepository.findAll()
			.stream()
			.map(member -> MemberResponseDTO.builder()
				.id(member.getId())
				.email(member.getEmail())
				.nickname(member.getNickname())
				.build())
			.toList();
	}

	@Override
	public MemberResponseDTO getMember(Long id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

		return MemberResponseDTO.builder()
			.id(member.getId())
			.email(member.getEmail())
			.nickname(member.getNickname())
			.build();
	}
}
