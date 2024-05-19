package com.greatlearning.employeemanagement.entity;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestEPLoggingEntity {

	private String methodType;
	private String url;
	private Map<String, String> pathVariables;
	private Map<String, List<String>> requestparam;
	private String requestobject;

}
