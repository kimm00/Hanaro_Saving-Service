package com.hana8.hanaro.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hana8.hanaro.dto.account.AccountResponseDTO;
import com.hana8.hanaro.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

	// Entity → DTO
	@Mapping(source = "member.id", target = "memberId")
	@Mapping(target = "productId", expression = "java(getProductId(account))")
	AccountResponseDTO toDTO(Account account);

	default Long getProductId(Account account) {
		if (account.getSubscription() == null)
			return null;
		if (account.getSubscription().getProduct() == null)
			return null;
		return account.getSubscription().getProduct().getId();
	}
}
