package com.hana8.hanaro.service;

import java.util.List;

import com.hana8.hanaro.dto.member.MemberResponseDTO;

public interface MemberService {
	MemberResponseDTO register(String email, String password, String nickname);

	MemberResponseDTO login(String email, String password);

	List<MemberResponseDTO> getMembers();

	MemberResponseDTO getMember(Long id);
}
