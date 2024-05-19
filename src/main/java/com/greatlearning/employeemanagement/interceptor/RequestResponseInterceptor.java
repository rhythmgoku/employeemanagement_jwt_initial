package com.greatlearning.employeemanagement.interceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.greatlearning.employeemanagement.entity.RestEPLoggingEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RequestResponseInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {

		RestEPLoggingEntity epLoggingEntity = formRestEPLoggingEntity(request);

		log.info("Initiating Api Call for Service for Method -- " + " [" + epLoggingEntity.getMethodType() + "] --  [ "
				+ epLoggingEntity.getUrl() + " ] -- with params - " + " [" + epLoggingEntity.getRequestparam() + "]"
				+ " -- path variables -- [" + epLoggingEntity.getPathVariables() + "]");
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws IOException {

		RestEPLoggingEntity epLoggingEntity = formRestEPLoggingEntity(request);

		log.info("Completed Api Call for Service for" + " [" + epLoggingEntity.getMethodType() + "] --  [ "
				+ epLoggingEntity.getUrl() + " ]");

	}

	private RestEPLoggingEntity formRestEPLoggingEntity(HttpServletRequest request) throws IOException {

		@SuppressWarnings("unchecked")
		final Map<String, String> pathVariablesMap = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		@SuppressWarnings("unused")
		final Map<String, List<String>> headersMap = Collections.list(request.getHeaderNames()).stream()
				.collect(Collectors.toMap(Function.identity(), header -> Collections.list(request.getHeaders(header))));

		final Map<String, List<String>> paramsMaps = new HashMap<>(request.getParameterMap()).entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> Arrays.asList(entry.getValue())));

		return RestEPLoggingEntity.builder().methodType(request.getMethod()).url(request.getRequestURI())
				.pathVariables(pathVariablesMap).requestparam(paramsMaps).build();
	}

}
