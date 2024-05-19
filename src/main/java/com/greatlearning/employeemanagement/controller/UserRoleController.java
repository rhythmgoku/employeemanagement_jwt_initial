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
import org.springframework.web.bind.annotation.RestController;

import com.greatlearning.employeemanagement.entity.Roles;
import com.greatlearning.employeemanagement.exception.CustomBusinessException;
import com.greatlearning.employeemanagement.services.CustomUserRolesDetailsService;

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

@RequestMapping(value = "/roles")
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "4. UserRole Management API")
public class UserRoleController {

	@Autowired
	CustomUserRolesDetailsService customUserRolesDetailsService;

	@GetMapping
	@Operation(summary = "Get All UserRoles", description = "Returns List of UserRoles")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Roles.class)), examples = {
					@ExampleObject(name = "1", value = "[ { \"id\": 1, \"name\": \"ADMIN\" }, { \"id\": 2, \"name\": \"USER\" }, { \"id\": 3, \"name\": \"SUPERUSER\" } ]") }) }) })
	public ResponseEntity<List<Roles>> getAllUserRoles() throws CustomBusinessException {
		List<Roles> roles = customUserRolesDetailsService.getAllUserRoles();
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}

	@PostMapping("/add")
	@Operation(summary = "Add UserRole", description = "Return newly added UserRole")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Added", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Roles.class), examples = {
					@ExampleObject(name = "1", value = "{\"name\":\"SUPERUSER\"}") }) }) })
	public ResponseEntity<Roles> addUserRole(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Role object to create", required = true, content = @Content(schema = @Schema(implementation = Roles.class), examples = {
					@ExampleObject(name = "1", value = "{ \"id\": 3, \"name\": \"SUPERUSER\" }") })) @Valid @RequestBody Roles role)
			throws CustomBusinessException {
		return new ResponseEntity<>(customUserRolesDetailsService.saveUserRole(role), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Find UserRole by Id", description = "Return UserRole by Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Roles.class), examples = {
					@ExampleObject(name = "1", value = "{ \"id\": 1, \"name\": \"ADMIN\" }") }) }) })
	public ResponseEntity<Roles> findUserRoleById(
			@Parameter(name = "id", description = "User Role id", example = "1") @PathVariable("id") Integer id)
			throws CustomBusinessException {
		Roles role = customUserRolesDetailsService.findUserRoleById(id);
		return new ResponseEntity<>(role, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Edit UserRole", description = "Edits the UserRole by Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Edited", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Roles.class), examples = {
					@ExampleObject(name = "1", value = "{ \"id\": 1, \"name\": \"ADMIN\" }") }) }) })
	public ResponseEntity<Roles> editUserRole(
			@Parameter(name = "id", description = "User Role id", example = "1") @PathVariable("id") Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Role object to edit", required = true, content = @Content(schema = @Schema(implementation = Roles.class), examples = {
					@ExampleObject(name = "1", value = "{ \"id\": 1, \"name\": \"ADMIN\" }") })) @Valid @RequestBody Roles role)
			throws CustomBusinessException {
		Roles editedRole = null;
		editedRole = customUserRolesDetailsService.editUserRole(role);
		return new ResponseEntity<>(editedRole, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete UserRole", description = "Delete UserRole by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Deleted", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = {
					@ExampleObject(name = "1", value = "Deleted UserRole with id - 1") }) }) })
	public ResponseEntity<String> deleteUserRole(
			@Parameter(name = "id", description = "User Role id", example = "1") @PathVariable("id") Integer id)
			throws CustomBusinessException {
		boolean satus = customUserRolesDetailsService.deleteUserRole(id);
		return new ResponseEntity<>(
				satus ? ("Deleted UserRole with id - " + id) : ("Unable to Delete UserRole with id - " + id),
				HttpStatus.OK);
	}

}
