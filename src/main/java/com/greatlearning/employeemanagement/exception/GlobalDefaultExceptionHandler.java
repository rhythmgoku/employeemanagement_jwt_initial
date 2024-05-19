package com.greatlearning.employeemanagement.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

	@ExceptionHandler(value = CustomBusinessException.class)
	public ResponseEntity<CustomBusinessErrorResponse> customBusinessExceptionErrorHandler(HttpServletRequest request,
			CustomBusinessException businessException) {

		CustomBusinessErrorResponse customBusinessErrorResponse = CustomBusinessErrorResponse.builder()
				.code(businessException.getCode()).message(businessException.getMessage())
				.status(businessException.getStatus()).timestamp(businessException.getTimestamp()).build();

		return new ResponseEntity<>(customBusinessErrorResponse,
				HttpStatusCode.valueOf(customBusinessErrorResponse.getCode()));
	}

	@ExceptionHandler(value = AccessDeniedException.class)
	public ResponseEntity<CustomBusinessErrorResponse> accessDeniedExceptionErrorHandler(HttpServletRequest request,
			AccessDeniedException accessDeniedException) {

		CustomBusinessErrorResponse customBusinessErrorResponse = CustomBusinessErrorResponse.builder()
				.code(HttpStatus.FORBIDDEN.value()).message("You don't have enough privileges to perform this action")
				.status(HttpStatus.FORBIDDEN.toString()).timestamp(new Date()).build();

		return new ResponseEntity<>(customBusinessErrorResponse,
				HttpStatusCode.valueOf(customBusinessErrorResponse.getCode()));
	}

	@ExceptionHandler(value = UsernameNotFoundException.class)
	public ResponseEntity<CustomBusinessErrorResponse> usernameNotFoundExceptionErrorHandler(HttpServletRequest request,
			UsernameNotFoundException usernameNotFoundException) {

		CustomBusinessErrorResponse customBusinessErrorResponse = CustomBusinessErrorResponse.builder()
				.code(HttpStatus.FORBIDDEN.value()).message("UserName Not found in the System or the Token is Invalid")
				.status(HttpStatus.FORBIDDEN.toString()).timestamp(new Date()).build();

		return new ResponseEntity<>(customBusinessErrorResponse,
				HttpStatusCode.valueOf(customBusinessErrorResponse.getCode()));
	}

	@ExceptionHandler(value = SignatureException.class)
	public ResponseEntity<CustomBusinessErrorResponse> signatureExceptionErrorHandler(HttpServletRequest request,
			SignatureException signatureException) {

		CustomBusinessErrorResponse customBusinessErrorResponse = CustomBusinessErrorResponse.builder()
				.code(HttpStatus.FORBIDDEN.value())
				.message(
						"Token is invalid or expired, Please generate a new one,  More meta info -- JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted ")
				.status(HttpStatus.FORBIDDEN.toString()).timestamp(new Date()).build();

		return new ResponseEntity<>(customBusinessErrorResponse,
				HttpStatusCode.valueOf(customBusinessErrorResponse.getCode()));
	}

	@ExceptionHandler(value = AuthenticationException.class)
	public ResponseEntity<CustomBusinessErrorResponse> signatureExceptionErrorHandler(HttpServletRequest request,
			AuthenticationException authenticationException) {

		CustomBusinessErrorResponse customBusinessErrorResponse = CustomBusinessErrorResponse.builder()
				.code(HttpStatus.FORBIDDEN.value())
				.message("Authentication Header or JWT Authentication Token is Not Present -- More Info  =>  "
						+ authenticationException.getMessage() + " Exception Type --  "
						+ authenticationException.getClass().getSimpleName())
				.status(HttpStatus.FORBIDDEN.toString()).timestamp(new Date()).build();

		return new ResponseEntity<>(customBusinessErrorResponse,
				HttpStatusCode.valueOf(customBusinessErrorResponse.getCode()));
	}

	@ExceptionHandler(value = ExpiredJwtException.class)
	public ResponseEntity<CustomBusinessErrorResponse> expiredJwtExceptionErrorHandler(HttpServletRequest request,
			ExpiredJwtException expiredJwtException) {

		CustomBusinessErrorResponse customBusinessErrorResponse = CustomBusinessErrorResponse.builder()
				.code(HttpStatus.EXPECTATION_FAILED.value())
				.message(expiredJwtException.getMessage() + " Exception Type --  "
						+ expiredJwtException.getClass().getSimpleName())
				.status(HttpStatus.EXPECTATION_FAILED.toString()).timestamp(new Date()).build();

		return new ResponseEntity<>(customBusinessErrorResponse, HttpStatus.EXPECTATION_FAILED);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<CustomBusinessErrorResponse> defaultErrorHandler(HttpServletRequest request,
			Exception exception) {

		CustomBusinessErrorResponse customBusinessErrorResponse = CustomBusinessErrorResponse.builder()
				.code(HttpStatus.EXPECTATION_FAILED.value())
				.message(exception.getMessage() + " Exception Type --  " + exception.getClass().getSimpleName())
				.status(HttpStatus.EXPECTATION_FAILED.toString()).timestamp(new Date()).build();

		return new ResponseEntity<>(customBusinessErrorResponse, HttpStatus.EXPECTATION_FAILED);
	}

}