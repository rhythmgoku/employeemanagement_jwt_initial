package com.greatlearning.employeemanagement.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.greatlearning.employeemanagement.services.CustomUserDetailsService;
import com.greatlearning.employeemanagement.services.JWTAuthenticationService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	JWTAuthenticationService jwtAuthenticationService;

	@Autowired
	HandlerExceptionResolver handlerExceptionResolver;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {

			final String authorizationHeader = request.getHeader("Authorization");

			String username = null;
			String jwt = null;

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);

				try {
					username = jwtAuthenticationService.extractUsername(jwt);
				} catch (ExpiredJwtException | SignatureException exception) {
					handlerExceptionResolver.resolveException(request, response, null, exception);
					return;
				}

			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails;

				try {
					userDetails = this.customUserDetailsService.loadUserByUsername(username);
				} catch (UsernameNotFoundException exception) {
					handlerExceptionResolver.resolveException(request, response, null, exception);
					return;
				}

				if (jwtAuthenticationService.validateToken(jwt, userDetails)) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}

			}

		} catch (Exception exception) {
			handlerExceptionResolver.resolveException(request, response, null, exception);
			return;
		}

		filterChain.doFilter(request, response);

	}

}
