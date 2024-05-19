package com.greatlearning.employeemanagement.services;

import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import com.greatlearning.employeemanagement.exception.CustomBusinessException;

import io.jsonwebtoken.Claims;

public interface JWTAuthenticationService {

	String generateToken(UserDetails userDetails);

	String extractUsername(String token);

	Date extractExpiration(String token);

	<T> T extractClaim(String token, Function<Claims, T> claimsResolver);

	boolean validateToken(String token, UserDetails userDetails);

	boolean tokenValidation(String token, UserDetails userDetails) throws CustomBusinessException;

}
