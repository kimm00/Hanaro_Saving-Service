package com.hana8.hanaro.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hana8.hanaro.common.enums.Role;
import com.hana8.hanaro.dto.member.LoginResponseDTO;
import com.hana8.hanaro.dto.member.MemberResponseDTO;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.mapper.MemberMapper;
import com.hana8.hanaro.repository.MemberRepository;
import com.hana8.hanaro.security.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final AccountService accountService;
	private final MemberMapper memberMapper;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public MemberResponseDTO register(String email, String password, String nickname) {
		log.info("[USER] 회원가입 시도 - email={}, nickname={}", email, nickname);

		if (memberRepository.existsByEmail(email)) {
			log.info("[USER] 회원가입 시도 - email={}, nickname={}", email, nickname);
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}

		Member member = Member.builder()
			.email(email)
			.password(passwordEncoder.encode(password))
			.nickname(nickname)
			.role(Role.ROLE_USER)
			.build();

		member = memberRepository.save(member);

		// 회원가입 시 자유입출금 계좌 자동 생성
		var accountDTO = accountService.createDefaultAccount(member.getId());

		log.info("[USER] 회원가입 성공 - memberId={}, email={}", member.getId(), email);

		// DTO에 accountNumber 넣어서 반환
		return MemberResponseDTO.builder()
			.id(member.getId())
			.email(member.getEmail())
			.nickname(member.getNickname())
			.accountNumber(accountDTO.getAccountNumber())
			.build();
	}

	public LoginResponseDTO login(String email, String password) {
		log.info("[USER] 로그인 시도 - email={}", email);

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> {
				log.warn("[USER] 로그인 실패 - 존재하지 않는 이메일: {}", email);
				return new IllegalArgumentException("회원이 존재하지 않습니다.");
			});

		if (!passwordEncoder.matches(password, member.getPassword())) {
			log.warn("[USER] 로그인 실패 - 비밀번호 불일치: {}", email);
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		// 로그인 성공 시 JWT 토큰 생성
		Map<String, Object> claims = Map.of(
			"email", member.getEmail(),
			"role", member.getRole().name()
		);

		String token = jwtUtil.generateToken(claims, 60); // 60분 유효한 토큰 생성

		log.info("[USER] 로그인 성공 - email={}", email);

		return LoginResponseDTO.builder()
			.token(token)
			.build();
	}

	public List<MemberResponseDTO> getMembers() {
		log.info("[USER] 전체 회원 조회");

		return memberRepository.findAll()
			.stream()
			.map(memberMapper::toDTO)
			.toList();
	}

	public MemberResponseDTO getMember(Long id) {
		log.info("[USER] 전체 회원 조회");

		Member member = memberRepository.findById(id)
			.orElseThrow(() -> {
				log.warn("[USER] 회원 조회 실패 - 존재하지 않는 id={}", id);
				return new IllegalArgumentException("회원이 존재하지 않습니다.");
			});
		return memberMapper.toDTO(member);
	}
}
