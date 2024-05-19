package com.greatlearning.employeemanagement.services;

import java.util.List;

import com.greatlearning.employeemanagement.entity.Employee;
import com.greatlearning.employeemanagement.exception.CustomBusinessException;

public interface EmployeeService {

	List<Employee> getAllEmployees() throws CustomBusinessException;;

	Employee saveEmployee(Employee employee) throws CustomBusinessException;

	Employee editEmployee(Employee editedEmployee) throws CustomBusinessException;

	boolean deleteEmployee(Integer id) throws CustomBusinessException;

	Employee findEmployeeById(Integer id) throws CustomBusinessException;

	List<Employee> findAllEmployeesAndOrderBy(String oderBy) throws CustomBusinessException;

	List<Employee> findAllEmployeeByFirstname(String firstName) throws CustomBusinessException;
}
