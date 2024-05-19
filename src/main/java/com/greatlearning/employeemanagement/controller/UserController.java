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

import com.greatlearning.employeemanagement.entity.Users;
import com.greatlearning.employeemanagement.exception.CustomBusinessException;
import com.greatlearning.employeemanagement.services.CustomUserDetailsService;

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

@RequestMapping(value = "/users")
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "3. User Management API")
public class UserController {

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@GetMapping
	@Operation(summary = "Get All Users", description = "Returns List of Users")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Users.class)), examples = {
					@ExampleObject(name = "1", value = "[ { \"username\": \"admin\", \"password\": \"admin\", \"roles\": [ { \"id\": 1, \"name\": \"ADMIN\" } ] }, { \"username\": \"superadmin\", \"password\": \"superadmin\", \"roles\": [ { \"id\": 1, \"name\": \"ADMIN\" } ] }, { \"username\": \"user\", \"password\": \"user\", \"roles\": [ { \"id\": 2, \"name\": \"USER\" } ] }, { \"username\": \"testuser\", \"password\": \"testuser\", \"roles\": [ { \"id\": 2, \"name\": \"USER\" } ] } ]") }) }) })
	public ResponseEntity<List<Users>> getAllUsers() throws CustomBusinessException {
		List<Users> users = customUserDetailsService.getAllUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PostMapping("/add")
	@Operation(summary = "Add User", description = "Return newly added User")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Added", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Users.class), examples = {
					@ExampleObject(name = "1", value = "{\"username\":\"Magnus\",\"password\":\"Magnus12345\",\"roles\":[{\"id\":3,\"name\":\"SUPERUSER\"}]}") }) }) })
	public ResponseEntity<Users> addUser(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User object to create", required = true, content = @Content(schema = @Schema(implementation = Users.class), examples = {
					@ExampleObject(name = "1", value = "{ \"username\": \"Magnus\", \"password\": \"Magnus12345\", \"roles\": [ { \"id\": 3, \"name\": \"SUPERUSER\" } ] }") })) @Valid @RequestBody Users user)
			throws CustomBusinessException {
		Users savedUser = customUserDetailsService.saveUser(user);
		return new ResponseEntity<>(savedUser, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Find User by Id", description = "Returnd User by Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Users.class), examples = {
					@ExampleObject(name = "1", value = "{ \"username\": \"admin\", \"password\": \"admin\", \"roles\": [ { \"id\": 1, \"name\": \"ADMIN\" } ] }") }) }) })
	public ResponseEntity<Users> findUserById(
			@Parameter(name = "id", description = "User id", example = "1") @PathVariable("id") Integer id)
			throws CustomBusinessException {
		Users user = customUserDetailsService.findUserById(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Edit User", description = "Edits the User by Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Edited", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Users.class), examples = {
					@ExampleObject(name = "1", value = "{ \"id\" : 1, \"username\":\"Magnus\", \"password\":\"Magnus12345\", \"roles\":[{ \"id\":3, \"name\":\"SUPERUSER\" }] }") }) }) })
	public ResponseEntity<Users> editUser(
			@Parameter(name = "id", description = "User id", example = "1") @PathVariable("id") Integer id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User object to edit", required = true, content = @Content(schema = @Schema(implementation = Users.class), examples = {
					@ExampleObject(name = "1", value = "{ \"id\" : 1, \"username\":\"Magnus\", \"password\":\"Magnus12345\", \"roles\":[{ \"id\":3, \"name\":\"SUPERUSER\" }] }") })) @Valid @RequestBody Users user)
			throws CustomBusinessException {
		Users editedUser = null;
		editedUser = customUserDetailsService.editUser(user);
		return new ResponseEntity<>(editedUser, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete User", description = "Delete User by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Deleted", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = {
					@ExampleObject(name = "1", value = "Deleted User with id - 1") }) }) })
	public ResponseEntity<String> deleteUser(
			@Parameter(name = "id", description = "User id", example = "1") @PathVariable("id") Integer id)
			throws CustomBusinessException {
		boolean satus = customUserDetailsService.deleteUser(id);
		return new ResponseEntity<>(
				satus ? ("Deleted User with id - " + id) : ("Unable to Delete User with id - " + id), HttpStatus.OK);
	}

}
