package com.greatlearning.employeemanagement.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.greatlearning.employeemanagement.entity.Users;
import com.greatlearning.employeemanagement.exception.CustomBusinessException;

public interface CustomUserDetailsService {

	Users saveUser(Users user) throws CustomBusinessException;

	Users editUser(Users user) throws CustomBusinessException;

	List<Users> getAllUsers() throws CustomBusinessException;

	Users findUserById(Integer id) throws CustomBusinessException;

	boolean deleteUser(Integer id) throws CustomBusinessException;

	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
