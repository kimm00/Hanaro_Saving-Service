package com.hana8.hanaro.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hana8.hanaro.dto.account.AccountResponseDTO;
import com.hana8.hanaro.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

	// Entity → DTO
	@Mapping(source = "member.id", target = "memberId")
	AccountResponseDTO toDTO(Account account);
}
