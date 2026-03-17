package com.hana8.hanaro.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {
	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Size(min = 8, max = 20)
	private String password;

	@NotBlank
	@Size(min = 2, max = 20)
	private String nickname;

}
