package com.hana8.hanaro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
	@NotBlank(message = "이메일은 필수입니다!")
	@Email(message = "올바른 이메일 형식이어야 합니다.")
	@Schema(description = "이메일", example = "sample@gmail.com")
	@NotBlank String email,

	@Schema(description = "비밀번호", example = "pw001234")
	@NotBlank String password
) {
}
