package com.hana8.hanaro.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hana8.hanaro.entity.Member;
import com.hana8.hanaro.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final MemberRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("*** Member 로그인 시도 = " + username);

		Member member = repository.findByEmail(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return User.builder()
			.username(member.getEmail())
			.password(member.getPassword())
			.authorities(member.getRole().name())
			.build();
	}
}
