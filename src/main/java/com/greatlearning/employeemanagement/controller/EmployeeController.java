package com.greatlearning.employeemanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greatlearning.employeemanagement.entity.Employee;
import com.greatlearning.employeemanagement.exception.CustomBusinessException;
import com.greatlearning.employeemanagement.services.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping("/employees")
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "2. Employee Management API")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping
	@Operation(summary = "Get All Employees", description = "Returns List of Employees")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Employee.class)), examples = {
					@ExampleObject(name = "1", value = "[ { \"id\": 1, \"firstName\": \"Ram\", \"lastName\": \"Pandit\", \"email\": \"Pandit.Ram@gamil.com\" }, { \"id\": 2, \"firstName\": \"Rahul\", \"lastName\": \"Tiwari\", \"email\": \"Tiwari.Rahul@gamil.com\" }, { \"id\": 3, \"firstName\": \"Mohit\", \"lastName\": \"Shukla\", \"email\": \"Shukla.Mohit@gamil.com\" }, { \"id\": 4, \"firstName\": \"Ankita\", \"lastName\": \"Yadav\", \"email\": \"Yadav.Ankita@gamil.com\" } ]") }) }) })
	public ResponseEntity<List<Employee>> getAllEmployees() throws CustomBusinessException {
		List<Employee> employees = employeeService.getAllEmployees();
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@PostMapping(value = "/add")
	@Operation(summary = "Add Employee", description = "Return newly added Employee")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Added", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class), examples = {
					@ExampleObject(name = "1", value = "{ \"id\": 1, \"firstName\": \"Rahul\", \"lastName\": \"Pandey\", \"email\": \"pandey.rahul@gl.com\" }") }) }) , @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = {
							@Content(mediaType = "application/json", schema = @Schema(implementation = CustomBusinessException.class), examples = {
									@ExampleObject(name = "1", value = "{ \"timestamp\": \"20-05-2024 07:43:53\", \"code\": 403, \"status\": \"403 FORBIDDEN\", \"message\": \"You don't have enough privileges to perform this action\" }") }) })})
	public ResponseEntity<Employee> addEmployee(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Employee object to create", required = true, content = @Content(schema = @Schema(implementation = Employee.class), examples = {
					@ExampleObject(name = "1", value = "{ \"firstName\": \"Rahul\", \"lastName\": \"Pandey\", \"email\": \"pandey.rahul@gl.com\" }") })) @Valid @RequestBody Employee employee)
			throws CustomBusinessException {
		Employee savedEmployee = employeeService.saveEmployee(employee);
		return new ResponseEntity<>(savedEmployee, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "Find Employee by Id", description = "Returnd Employee by Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class), examples = {
					@ExampleObject(name = "1", value = "{ \"id\": 1, \"firstName\": \"Rahul\", \"lastName\": \"Pandey\", \"email\": \"pandey.rahul@gl.com\" }") }) }) , @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = {
							@Content(mediaType = "application/json", schema = @Schema(implementation = CustomBusinessException.class), examples = {
									@ExampleObject(name = "1", value = "{ \"timestamp\": \"20-05-2024 07:43:53\", \"code\": 403, \"status\": \"403 FORBIDDEN\", \"message\": \"You don't have enough privileges to perform this action\" }") }) }) })
	public ResponseEntity<Employee> findEmployeeById(
			@Parameter(name = "id", description = "Employee id", example = "1") @PathVariable("id") Integer id)
			throws CustomBusinessException {
		Employee employee = employeeService.findEmployeeById(id);
		return new ResponseEntity<>(employee, HttpStatus.OK);
	}

	@PutMapping(value = "/{id}")
	@Operation(summary = "Edit Employee", description = "Edits the Employee by Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Edited", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class), examples = {
					@ExampleObject(name = "1", value = "{ \"id\": 1, \"firstName\": \"Rahul\", \"lastName\": \"Pandey\", \"email\": \"pandey.rahul@gl.com\" }") }) }) , @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = {
							@Content(mediaType = "application/json", schema = @Schema(implementation = CustomBusinessException.class), examples = {
									@ExampleObject(name = "1", value = "{ \"timestamp\": \"20-05-2024 07:43:53\", \"code\": 403, \"status\": \"403 FORBIDDEN\", \"message\": \"You don't have enough privileges to perform this action\" }") }) }) })
	public ResponseEntity<Employee> editEmployee(
			@Parameter(name = "id", description = "Employee id", example = "1") @PathVariable("id") Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Employee object to edit", required = true, content = @Content(schema = @Schema(implementation = Employee.class), examples = {
					@ExampleObject(name = "1", value = "{ \"id\": 1, \"firstName\": \"Rahul\", \"lastName\": \"Pandey\", \"email\": \"pandey.rahul@gl.com\" }") })) @Valid @RequestBody Employee employee)
			throws CustomBusinessException {
		Employee editedEmployee = null;
		editedEmployee = employeeService.editEmployee(employee);
		return new ResponseEntity<>(editedEmployee, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Delete Employee", description = "Delete Employee by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Deleted", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = {
					@ExampleObject(name = "1", value = "Deleted Employee with id - 1") }) }) , @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = {
							@Content(mediaType = "application/json", schema = @Schema(implementation = CustomBusinessException.class), examples = {
									@ExampleObject(name = "1", value = "{ \"timestamp\": \"20-05-2024 07:43:53\", \"code\": 403, \"status\": \"403 FORBIDDEN\", \"message\": \"You don't have enough privileges to perform this action\" }") }) }) })
	public ResponseEntity<String> deleteEmployee(
			@Parameter(name = "id", description = "Employee id", example = "1") @PathVariable("id") Integer id)
			throws CustomBusinessException {
		boolean satus = employeeService.deleteEmployee(id);
		return new ResponseEntity<>(
				satus ? ("Deleted Employee with id - " + id) : ("Unable to Delete Employee with id - " + id),
				HttpStatus.OK);
	}

	@GetMapping(value = "/search/{keyword}")
	@Operation(summary = "Search Employees by their First Name ", description = "find all the Employees by First Name")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Employee.class)), examples = {
					@ExampleObject(name = "1", value = "[ { \"id\": 1, \"firstName\": \"Ram\", \"lastName\": \"Pandit\", \"email\": \"Pandit.Ram@gamil.com\" }, { \"id\": 2, \"firstName\": \"Rahul\", \"lastName\": \"Tiwari\", \"email\": \"Tiwari.Rahul@gamil.com\" } ]") }) }) })
	public ResponseEntity<List<Employee>> searchByFirstname(
			@Parameter(name = "keyword", description = "keyword to search in firstname", example = "Ra") @PathVariable("keyword") String keyword)
			throws CustomBusinessException {
		List<Employee> employees = employeeService.findAllEmployeeByFirstname(keyword);
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@GetMapping(value = "/sort")
	@Operation(summary = "Get All Employees with Sort order as Asc or Desc ", description = "find all the Employees and sort by asc or desc")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Employee.class)), examples = {
					@ExampleObject(name = "1", value = "[ { \"id\": 4, \"firstName\": \"Ankita\", \"lastName\": \"Yadav\", \"email\": \"Yadav.Ankita@gamil.com\" }, { \"id\": 3, \"firstName\": \"Mohit\", \"lastName\": \"Shukla\", \"email\": \"Shukla.Mohit@gamil.com\" }, { \"id\": 2, \"firstName\": \"Rahul\", \"lastName\": \"Tiwari\", \"email\": \"Tiwari.Rahul@gamil.com\" }, { \"id\": 1, \"firstName\": \"Ram\", \"lastName\": \"Pandit\", \"email\": \"Pandit.Ram@gamil.com\" } ]") }) }) })
	public ResponseEntity<List<Employee>> sortAndGetAllEmployees(
			@Parameter(name = "order", description = "sort all record by asc and desc", example = "asc") @RequestParam(value = "order", required = true) String order)
			throws CustomBusinessException {
		List<Employee> employees = employeeService.findAllEmployeesAndOrderBy(order);
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

}
