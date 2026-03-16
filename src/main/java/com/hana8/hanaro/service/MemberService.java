package com.hana8.hanaro.service;

import java.util.List;

import com.hana8.hanaro.entity.Member;

public interface MemberService {
	Member register(String email, String password, String nickname);

	Member login(String email, String password);

	List<Member> getMembers();

	Member getMember(Long id);
}
