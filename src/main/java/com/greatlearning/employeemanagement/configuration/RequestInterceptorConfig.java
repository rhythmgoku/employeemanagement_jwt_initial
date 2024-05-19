package com.greatlearning.employeemanagement.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.greatlearning.employeemanagement.interceptor.RequestResponseInterceptor;

@Configuration
public class RequestInterceptorConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RequestResponseInterceptor());
	}
}
