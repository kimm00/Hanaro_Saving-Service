package com.hana8.hanaro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> b70aeda18ad66a4c0fc4e7455f9f211b5c41e237
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

<<<<<<< HEAD
import com.hana8.hanaro.common.enums.Role;
=======
>>>>>>> b70aeda18ad66a4c0fc4e7455f9f211b5c41e237
import com.hana8.hanaro.dto.account.AccountResponseDTO;
import com.hana8.hanaro.dto.member.MemberResponseDTO;
import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.mapper.MemberMapper;
import com.hana8.hanaro.repository.MemberRepository;
import com.hana8.hanaro.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	private MemberRepository memberRepository;
	@Mock
	private AccountService accountService;
	@Mock
	private MemberMapper memberMapper;
	@Mock
	private JwtUtil jwtUtil;
	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private MemberService memberService;

	@Test
	void register() {
		assertThrows(Exception.class,
			() -> memberService.register("test@test.com", "1234", "nick"));
	}

	@Test
	void register_success() {

		Member member = Member.builder()
			.id(1L)
			.email("test@test.com")
			.nickname("nick")
			.build();

		when(memberRepository.existsByEmail(anyString()))
			.thenReturn(false);

		when(passwordEncoder.encode(anyString()))
			.thenReturn("encoded");

		when(memberRepository.save(any()))
			.thenReturn(member);

		when(accountService.createDefaultAccount(any()))
			.thenReturn(mock(AccountResponseDTO.class));

		var result = memberService.register("test@test.com", "1234", "nick");

		assertEquals("test@test.com", result.getEmail());
	}

	@Test
	void login() {
		assertThrows(Exception.class,
			() -> memberService.login("test@test.com", "1234"));
	}

	@Test
	void getMembers() {
		memberService.getMembers();
		verify(memberRepository).findAll();
	}

	@Test
	void getMember_success() {

		Member member = Member.builder()
			.id(1L)
			.email("test@test.com")
			.nickname("nick")
			.build();

		when(memberRepository.findById(1L))
			.thenReturn(Optional.of(member));

		when(memberMapper.toDTO(member))
			.thenReturn(
				new MemberResponseDTO(1L, "test@test.com", "nick", null)
			);

		var result = memberService.getMember(1L);

		assertEquals("test@test.com", result.getEmail());
	}

	@Test
	void getMember() {
		assertThrows(Exception.class,
			() -> memberService.getMember(1L));
	}
<<<<<<< HEAD

	@Test
	void register_duplicateEmail() {

		when(memberRepository.existsByEmail(anyString()))
			.thenReturn(true);

		assertThrows(IllegalArgumentException.class,
			() -> memberService.register("test@test.com", "1234", "nick"));
	}

	@Test
	void login_success() {

		Member member = Member.builder()
			.email("test@test.com")
			.password("encoded")
			.role(Role.ROLE_USER)
			.build();

		when(memberRepository.findByEmail(anyString()))
			.thenReturn(Optional.of(member));

		when(passwordEncoder.matches(anyString(), anyString()))
			.thenReturn(true);

		when(jwtUtil.generateToken(anyMap(), anyInt()))
			.thenReturn("mock-token");

		var result = memberService.login("test@test.com", "12345678");

		assertEquals("mock-token", result.getToken());
	}

	@Test
	void login_wrongPassword() {

		Member member = Member.builder()
			.email("test@test.com")
			.password("encoded")
			.build();

		when(memberRepository.findByEmail(anyString()))
			.thenReturn(Optional.of(member));

		when(passwordEncoder.matches(anyString(), anyString()))
			.thenReturn(false);

		assertThrows(IllegalArgumentException.class,
			() -> memberService.login("test@test.com", "1234"));
	}

	@Test
	void login_emailNotFound() {

		when(memberRepository.findByEmail(anyString()))
			.thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class,
			() -> memberService.login("test@test.com", "1234"));
	}

	@Test
	void getMembers_result() {

		Member member = Member.builder()
			.id(1L)
			.email("test@test.com")
			.nickname("nick")
			.build();

		when(memberRepository.findAll())
			.thenReturn(List.of(member));

		when(memberMapper.toDTO(member))
			.thenReturn(new MemberResponseDTO(1L, "test@test.com", "nick", null));

		var result = memberService.getMembers();

		assertEquals(1, result.size());
		assertEquals("test@test.com", result.get(0).getEmail());
	}
=======
>>>>>>> b70aeda18ad66a4c0fc4e7455f9f211b5c41e237
}
