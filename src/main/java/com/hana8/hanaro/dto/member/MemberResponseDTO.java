package com.hana8.hanaro.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemberResponseDTO {
	private Long id;
	private String email;
	private String nickname;
	private String accountNumber;
}
