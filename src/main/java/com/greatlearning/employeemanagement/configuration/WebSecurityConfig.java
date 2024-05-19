package com.greatlearning.employeemanagement.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.greatlearning.employeemanagement.exception.CustomAccessDeniedHandler;
import com.greatlearning.employeemanagement.exception.RestAuthenticationEntryPoint;
import com.greatlearning.employeemanagement.filter.JwtRequestFilter;
import com.greatlearning.employeemanagement.services.CustomUserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private static final String[] All_SWAGGER_EP = { "/v2/api-docs", "/v3/api-docs/**", "/swagger-ui.html",
	"/swagger-ui/**" };

	private static final String[] All_ALLOWED_PATHS = { "/employees/search/{keyword}", "/employees/sort", "/employees",
			"/employees/", "/employees/get-all-employees" };

	private static final String[] ONLY_ADMIN_PATHS = { "/users/{id}", "/roles/{id}", "/employees/{id}",
	"/employees/add" };
	private static final String[] WHITE_LIST_URL = { "/authenticate", "/validate-token", "/users/add", "/roles/add",
			"/users", "/users/", "/users/get-all-users", "/roles", "/roles/", "/roles/get-all-roles", "/h2-console/**",
	"/users/accessdenied" };

	private static final String ADMIN = "ADMIN";

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, RestAuthenticationEntryPoint restAuthenticationEntryPoint,
			JwtRequestFilter jwtRequestFilter, CustomUserDetailsServiceImpl customUserDetailsService) throws Exception {

		http.authorizeHttpRequests(requests -> requests.requestMatchers(WHITE_LIST_URL).permitAll()
				.requestMatchers(All_SWAGGER_EP).permitAll().requestMatchers(All_ALLOWED_PATHS).authenticated()
				.requestMatchers(ONLY_ADMIN_PATHS).hasAuthority(ADMIN))
		.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
		.exceptionHandling(
				exceptionHandling -> exceptionHandling.accessDeniedHandler(customAccessDeniedHandler())
				.authenticationEntryPoint(restAuthenticationEntryPoint))
		.sessionManagement(
				sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.csrf(csrf -> csrf.disable());

		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, CustomUserDetailsServiceImpl customUserDetailsService) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(customUserDetailsService)
		.passwordEncoder(noOpPasswordEncoder());
		return authenticationManagerBuilder.build();
	}

	@Bean
	public PasswordEncoder noOpPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public AccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();

	}

}