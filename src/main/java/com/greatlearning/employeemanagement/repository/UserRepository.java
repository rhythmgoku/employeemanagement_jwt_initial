package com.greatlearning.employeemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greatlearning.employeemanagement.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

	public Users findByUsername(String s);
}