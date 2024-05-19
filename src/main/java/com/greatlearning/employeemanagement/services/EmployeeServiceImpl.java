package com.greatlearning.employeemanagement.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.greatlearning.employeemanagement.entity.Employee;
import com.greatlearning.employeemanagement.exception.CustomBusinessException;
import com.greatlearning.employeemanagement.repository.EmployeeRepository;

import io.micrometer.common.util.StringUtils;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public List<Employee> getAllEmployees() throws CustomBusinessException {

		List<Employee> employees = new ArrayList<>();

		try {
			employees = employeeRepository.findAll();
		} catch (Exception e) {
			throw new CustomBusinessException("Unable to fecth All Employees from database",
					HttpStatus.EXPECTATION_FAILED);
		}

		return employees;
	}

	@Override
	public Employee saveEmployee(Employee employee) throws CustomBusinessException {

		// validating employee information
		validateEmployee(employee, true);

		// Saving the New employee
		Employee savedEmployee = null;
		try {
			savedEmployee = employeeRepository.save(employee);
		} catch (Exception e) {
			throw new CustomBusinessException("Unable to Save the Employee, Due to -- " + e.getLocalizedMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}

		return savedEmployee;
	}

	@Override
	public Employee editEmployee(Employee editedEmployee) throws CustomBusinessException {

		// validating employee information
		validateEmployee(editedEmployee, false);

		Employee fecthedEmployee = employeeRepository.findById(editedEmployee.getId()).orElse(null);

		// Employee Mapper for Coping the editedEmployee data to fecthedEmployee data
		employeeMapper(fecthedEmployee, editedEmployee);

		// Saving the Edited employee
		Employee savedEmployee = null;

		try {
			savedEmployee = employeeRepository.save(fecthedEmployee);
		} catch (Exception e) {
			throw new CustomBusinessException(
					"Unable to Save the Employee while editing, Due to -- " + e.getLocalizedMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}

		return savedEmployee;
	}

	@Override
	public Employee findEmployeeById(Integer id) throws CustomBusinessException {

		Employee employee = null;

		if (null != id && id > 0) {
			employee = employeeRepository.findById(id).orElse(null);
		} else {
			throw new CustomBusinessException("Invalid Employee Id Prvided", HttpStatus.NOT_ACCEPTABLE);
		}

		if (null == employee) {
			throw new CustomBusinessException("Employee with Provided Id - " + id + " -  is not found in the database.",
					HttpStatus.NOT_FOUND);
		}

		return employee;
	}

	private void employeeMapper(Employee fetchedEmployee, Employee editedEmployee) throws CustomBusinessException {

		if (null != editedEmployee && null != fetchedEmployee && fetchedEmployee.getId() == editedEmployee.getId()) {

			// Mapping FirstName and LastName

			fetchedEmployee.setFirstName(
					(StringUtils.isNotBlank(editedEmployee.getFirstName())) ? editedEmployee.getFirstName()
							: fetchedEmployee.getFirstName());

			fetchedEmployee
					.setLastName((StringUtils.isNotBlank(editedEmployee.getLastName())) ? editedEmployee.getLastName()
							: fetchedEmployee.getLastName());

			// Mapping Email

			if (StringUtils.isNotBlank(editedEmployee.getEmail())) {

				if (EmailValidator.getInstance().isValid(editedEmployee.getEmail())) {
					fetchedEmployee
							.setEmail((StringUtils.isNotBlank(editedEmployee.getEmail())) ? editedEmployee.getEmail()
									: fetchedEmployee.getEmail());

				} else {
					throw new CustomBusinessException(
							"Incorrect Email format !! Please Provide the Email Correctly and Try again",
							HttpStatus.NOT_ACCEPTABLE);
				}

			}

		}

	}

	private boolean validateEmployee(Employee employee, boolean creationStage) throws CustomBusinessException {

		if (null != employee) {
			if (StringUtils.isBlank(employee.getFirstName())) {
				throw new CustomBusinessException("FirstName missing !! Please Provide the FirstName and Try again",
						HttpStatus.NOT_ACCEPTABLE);
			}
			if (StringUtils.isBlank(employee.getLastName())) {
				throw new CustomBusinessException("LastName missing !! Please Provide the LastName and Try again",
						HttpStatus.NOT_ACCEPTABLE);
			}

			if (StringUtils.isBlank(employee.getEmail()) && EmailValidator.getInstance().isValid(employee.getEmail())) {
				throw new CustomBusinessException(
						"Email missing or Incorrect Email format !! Please Provide the Email Correctly and Try again",
						HttpStatus.NOT_ACCEPTABLE);
			}

			validateEmployeeExistence(employee, creationStage);

		}

		return true;

	}

	private void validateEmployeeExistence(Employee employee, boolean creationStage) throws CustomBusinessException {

		Employee fecthedEmployee = null;

		if (creationStage) {
			fecthedEmployee = employeeRepository.findByEmail(employee.getEmail());

			if (null != fecthedEmployee) {
				throw new CustomBusinessException(
						" Employee with a different id already exists in the database , Already Existing Id for this Employee -- "
								+ fecthedEmployee.getId() + " with email id -- " + fecthedEmployee.getEmail() + " ",
						HttpStatus.NOT_ACCEPTABLE);
			}
		} else {

			if (null == employee.getId() || employee.getId() <= 0) {
				throw new CustomBusinessException(
						"Id missing !! Please Provide the Id to the Edit an Employee and Try again, Note id cannot be '0' or Negative ",
						HttpStatus.NOT_ACCEPTABLE);
			}

			fecthedEmployee = employeeRepository.findById(employee.getId()).orElse(null);

			if (null == fecthedEmployee) {
				throw new CustomBusinessException(
						" Employee not found in the database , with id -- " + employee.getId(),
						HttpStatus.NOT_ACCEPTABLE);
			}
		}

	}

	@Override
	public boolean deleteEmployee(Integer id) throws CustomBusinessException {

		Employee employeeToBeDeleted = null;

		// Employee Availability Pre-Check
		employeeToBeDeleted = employeeRepository.findById(id).orElse(null);

		if (null == employeeToBeDeleted) {

			// throwing Custom Exception for fail Scenarios
			throw new CustomBusinessException("Employee with Provided Id - " + id + " -  is not found in the database.",
					HttpStatus.NOT_FOUND);
		}

		// Delete Operation
		employeeRepository.deleteById(id);

		// Employee Availability Post-Deletion-Check
		employeeToBeDeleted = employeeRepository.findById(id).orElse(null);

		if (null != employeeToBeDeleted) {

			// throwing Custom Exception for fail Scenarios
			throw new CustomBusinessException(
					"Employee with Provided Id - " + id
							+ " -  cannot be deleted, possible reasons DB error, Access Issue",
					HttpStatus.EXPECTATION_FAILED);
		}

		return true;
	}

	@Override
	public List<Employee> findAllEmployeesAndOrderBy(String oderBy) throws CustomBusinessException {

		List<Employee> employees = new ArrayList<>();

		switch (oderBy) {
		case "asc":
			employees.addAll(employeeRepository.findAll(Sort.by(Sort.Order.asc("firstName").ignoreCase())));
			break;

		case "desc":
			employees.addAll(employeeRepository.findAll(Sort.by(Sort.Order.desc("firstName").ignoreCase())));
			break;

		default:
			throw new CustomBusinessException("order by options can be 'desc' or 'asc' only",
					HttpStatus.NOT_ACCEPTABLE);
		}

		return employees;
	}

	@Override
	public List<Employee> findAllEmployeeByFirstname(String firstName) throws CustomBusinessException {

		List<Employee> employees = new ArrayList<>();
		employees.addAll(employeeRepository.findAllByFirstNameContainingOrderByIdAsc(firstName));

		return employees;
	}

}
