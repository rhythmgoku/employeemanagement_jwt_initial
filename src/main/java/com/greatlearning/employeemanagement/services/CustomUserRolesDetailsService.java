package com.greatlearning.employeemanagement.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.greatlearning.employeemanagement.entity.Roles;
import com.greatlearning.employeemanagement.exception.CustomBusinessException;

@Service
public interface CustomUserRolesDetailsService {

	Roles saveUserRole(Roles role) throws CustomBusinessException;

	List<Roles> getAllUserRoles() throws CustomBusinessException;

	Roles findUserRoleById(Integer id) throws CustomBusinessException;

	Roles editUserRole(Roles role) throws CustomBusinessException;

	boolean deleteUserRole(Integer id) throws CustomBusinessException;

}
