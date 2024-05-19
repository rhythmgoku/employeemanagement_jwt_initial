package com.greatlearning.employeemanagement.entity;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "JSON Web Token")
public class JsonWebToken {

	@Schema(example = "****************")
	private String token;

	@Schema(example = "true")
	private boolean valid;

	@Schema(example = "2024-05-19T19:47:42.000+00:00")
	private Date expirationDate;

	@Schema(example = "admin")
	private String userName;

	@Schema(example = "Created Successfully")
	private String message;

	public JsonWebToken(String token) {
		super();
		this.token = token;
	}

}
