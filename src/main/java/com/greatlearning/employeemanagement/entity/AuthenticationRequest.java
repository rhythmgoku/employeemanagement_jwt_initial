package com.greatlearning.employeemanagement.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Basic Authentication Request ")
public class AuthenticationRequest {

	@Schema(example = "admin")
	private String username;

	@Schema(example = "admin")
	private String password;

}
