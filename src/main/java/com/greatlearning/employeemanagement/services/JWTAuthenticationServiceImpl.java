package com.greatlearning.employeemanagement.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.greatlearning.employeemanagement.exception.CustomBusinessException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTAuthenticationServiceImpl implements JWTAuthenticationService {

	@Value("${jwt.secret.key}")
	private String secretKey;

	@Value("#{${jwt.tokenlife.hours:1}*60}")
	private Long tokenLife;

	@Override
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	@Override
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	@Override
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	@Override
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	@Override
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	@Override
	public boolean tokenValidation(String token, UserDetails userDetails) throws CustomBusinessException {
		final String username = extractUsername(token);

		if (StringUtils.isBlank(username)) {
			throw new CustomBusinessException("User Name Cannot be extracted from JWT", HttpStatus.NOT_ACCEPTABLE);
		}

		if (!username.equals(userDetails.getUsername())) {
			throw new CustomBusinessException("User Name can not be matched with JWT", HttpStatus.NOT_ACCEPTABLE);
		}

		if (isTokenExpired(token)) {
			throw new CustomBusinessException("Token Expired Please generate a new one", HttpStatus.NOT_ACCEPTABLE);
		}

		return true;

	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private String createToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * tokenLife))
				.setAudience("Java FSD Batch").setIssuer("Great Learning").signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}

}
