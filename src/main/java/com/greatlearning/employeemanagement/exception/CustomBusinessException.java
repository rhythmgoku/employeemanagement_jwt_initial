package com.greatlearning.employeemanagement.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomBusinessException extends Exception {

	private static final long serialVersionUID = 1L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	@Schema(example = "20-05-2024 07:43:53")
	private final Date timestamp;
	
	@Schema(example = "403")
	private final int code;

	@Schema(example = "403 FORBIDDEN")
	private final String status;

	@Schema(example = "You don't have enough privileges to perform this action")
	private final String message;

	public CustomBusinessException(Date timestamp, int code, String status, String message) {
		super();
		this.timestamp = new Date();
		this.code = code;
		this.status = status;
		this.message = message;
	}

	public CustomBusinessException(String message, HttpStatus httpStatus) {
		super();
		this.timestamp = new Date();
		this.code = httpStatus.value();
		this.status = httpStatus.name();
		this.message = message;
	}

}
