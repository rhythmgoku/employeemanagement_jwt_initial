package com.greatlearning.employeemanagement.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomBusinessException extends Exception {

	private static final long serialVersionUID = 1L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private Date timestamp;

	private int code;

	private String status;

	private String message;

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
