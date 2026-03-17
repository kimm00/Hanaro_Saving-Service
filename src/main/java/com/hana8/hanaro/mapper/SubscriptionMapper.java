package com.hana8.hanaro.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hana8.hanaro.dto.subscription.SubscriptionResponseDTO;
import com.hana8.hanaro.entity.Subscription;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

	@Mapping(source = "member.id", target = "memberId")
	@Mapping(source = "member.nickname", target = "memberName")

	@Mapping(source = "product.id", target = "productId")
	@Mapping(source = "product.name", target = "productName")

	@Mapping(source = "account.id", target = "accountId")
	SubscriptionResponseDTO toDTO(Subscription subscription);
}
