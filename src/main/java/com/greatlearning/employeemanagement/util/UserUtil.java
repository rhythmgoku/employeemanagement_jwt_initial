package com.greatlearning.employeemanagement.util;

import java.util.Map;
import java.util.Map.Entry;

import com.greatlearning.employeemanagement.entity.Roles;
import com.greatlearning.employeemanagement.entity.Users;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserUtil {

	String getHighestRole(Users user, Map<Integer, String> roleMap) {
		Entry<Integer, String> role = roleMap.entrySet().stream()
				.filter(entry -> user.getRoles().stream().map(Roles::getName).toList().contains(entry.getValue()))
				.min(Map.Entry.comparingByKey()).orElse(null);
		 
		 return null != role ? role.getValue() : "";
	}

}
