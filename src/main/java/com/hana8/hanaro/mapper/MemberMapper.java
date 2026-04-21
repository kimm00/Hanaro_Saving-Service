package com.hana8.hanaro.mapper;

import org.mapstruct.Mapper;

import com.hana8.hanaro.dto.member.MemberResponseDTO;
import com.hana8.hanaro.entity.Member;

@Mapper(componentModel = "spring")
public interface MemberMapper {

	MemberResponseDTO toDTO(Member member);
}
