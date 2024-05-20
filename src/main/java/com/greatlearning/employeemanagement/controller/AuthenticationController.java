package com.greatlearning.employeemanagement.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greatlearning.employeemanagement.entity.AuthenticationRequest;
import com.greatlearning.employeemanagement.entity.JsonWebToken;
import com.greatlearning.employeemanagement.exception.CustomBusinessException;
import com.greatlearning.employeemanagement.services.CustomUserDetailsService;
import com.greatlearning.employeemanagement.services.JWTAuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping(value = "/")
@RestController
@Tag(name = "1. Authentication API")
public class AuthenticationController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	JWTAuthenticationService jwtAuthenticationService;

	@PostMapping("/authenticate")
	@Operation(summary = "Get Auth Token", description = "Returns Auth taken based on provided Auth request")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = JsonWebToken.class), examples = {
					@ExampleObject(name = "1", value = "{ \"token\": \"******TOKEN******\", \"valid\": true, \"expirationDate\": \"2024-05-20T02:02:15.000+00:00\", \"userName\": \"admin\", \"message\": \"Created Successfully\" }") }) }) })
	public ResponseEntity<JsonWebToken> createAuthenticationToken(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "AuthenticationRequest object to validate", required = true, content = @Content(schema = @Schema(implementation = AuthenticationRequest.class), examples = {
					@ExampleObject(name = "1", value = "{\"username\":\"admin\",\"password\":\"admin\"}") })) @Valid @RequestBody AuthenticationRequest authenticationRequest)
			throws CustomBusinessException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new CustomBusinessException("Incorrect username or password", HttpStatus.UNAUTHORIZED);
		}

		final UserDetails userDetails = customUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtAuthenticationService.generateToken(userDetails);

		Date expirationDate = jwtAuthenticationService.extractExpiration(jwt);

		return new ResponseEntity<>(JsonWebToken.builder().token(jwt).userName(userDetails.getUsername()).valid(true)
				.expirationDate(expirationDate).message("Created Successfully").build(), HttpStatus.OK);
	}

	@Operation(summary = "Validate Auth Token", description = "Suceess if the token valid ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Validated", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = JsonWebToken.class), examples = {
					@ExampleObject(name = "1", value = "{ \"token\": \"******TOKEN******\", \"valid\": true, \"expirationDate\": \"2024-05-20T02:02:15.000+00:00\", \"userName\": \"admin\", \"message\": \"Valid json web token\" }") }) })
			})
	@PostMapping("/validate-token")
	public ResponseEntity<JsonWebToken> validateToken(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "JsonWebToken object to validate", required = true, content = @Content(schema = @Schema(implementation = JsonWebToken.class), examples = {
					@ExampleObject(name = "1", value = "{ \"token\": \"********TOKEN********\" }") })) @Valid @RequestBody JsonWebToken webToken)
			throws CustomBusinessException {

		UserDetails userDetails = customUserDetailsService
				.loadUserByUsername(jwtAuthenticationService.extractUsername(webToken.getToken()));

		boolean jwtStatus = jwtAuthenticationService.tokenValidation(webToken.getToken(), userDetails);
		Date expirationDate = null;
		JsonWebToken jsonWebToken = null;

		if (jwtStatus) {
			expirationDate = jwtAuthenticationService.extractExpiration(webToken.getToken());
			jsonWebToken = JsonWebToken.builder().token(webToken.getToken()).userName(userDetails.getUsername())
					.valid(jwtStatus).expirationDate(expirationDate).message("Valid json web token").build();
		} else {
			jsonWebToken = JsonWebToken.builder().token(webToken.getToken()).userName(userDetails.getUsername())
					.valid(jwtStatus).expirationDate(expirationDate)
					.message("invalid token please create a new one and try again").build();
		}

		return new ResponseEntity<>(jsonWebToken, HttpStatus.OK);
	}

}
