package com.greatlearning.employeemanagement.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.greatlearning.employeemanagement.entity.Roles;
import com.greatlearning.employeemanagement.entity.Users;
import com.greatlearning.employeemanagement.exception.CustomBusinessException;
import com.greatlearning.employeemanagement.repository.RoleRepository;
import com.greatlearning.employeemanagement.repository.UserRepository;

import io.micrometer.common.util.StringUtils;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService, UserDetailsService {

	@Autowired
	UserRepository customUserRepository;

	@Autowired
	RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users customUser = customUserRepository.findByUsername(username);
		if (customUser == null) {
			throw new UsernameNotFoundException("User Not Found");
		}
		return customUser;
	}

	@Override
	public Users saveUser(Users user) throws CustomBusinessException {

		// validating user information
		validateUser(user, true);

		// Setting Default values like AccountEnabledStatus, AccountLockedStatus ,
		// AccountExpiryDate and CredentialsExpiryDate
		userDeafultValueSetter(user);

		// Saving the New User
		Users saveduser = null;
		try {
			saveduser = customUserRepository.save(user);
		} catch (Exception e) {
			throw new CustomBusinessException("Unable to Save the User with UserName", HttpStatus.EXPECTATION_FAILED);
		}

		return saveduser;
	}

	private void userDeafultValueSetter(Users user) {

		if (null != user) {
			user.setAccountEnabledStatus(1);
			user.setAccountExpiryDate(LocalDate.now().plusDays(5));
			user.setAccountLockedStatus(1);
			user.setCredentialsExpiryDate(LocalDate.now().plusDays(5));
		}
	}

	@Override
	public List<Users> getAllUsers() throws CustomBusinessException {

		List<Users> users = new ArrayList<>();

		try {
			users = customUserRepository.findAll();
		} catch (Exception e) {
			throw new CustomBusinessException(
					"Unable to fecth All Users from database, reason - " + e.getLocalizedMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}

		return users;
	}

	@Override
	public Users editUser(Users editeduser) throws CustomBusinessException {

		validateUser(editeduser, false);

		Users fecthedUser = customUserRepository.findById(editeduser.getId()).orElse(null);

		// User Mapper for Coping the editeduser data to fecthedUser data
		userMapper(fecthedUser, editeduser);

		if (null != fecthedUser) {
			fecthedUser = customUserRepository.save(fecthedUser);
		}

		return fecthedUser;
	}

	@Override
	public Users findUserById(Integer id) throws CustomBusinessException {

		Users user = null;

		if (null != id && id > 0) {
			user = customUserRepository.findById(id).orElse(null);
		} else {
			throw new CustomBusinessException("Invalid User Id Prvided", HttpStatus.EXPECTATION_FAILED);
		}

		if (null == user) {
			throw new CustomBusinessException("User with Provided Id - " + id + " -  is not found in the database.",
					HttpStatus.NOT_FOUND);
		}

		return user;
	}

	private void userMapper(Users fetchedUser, Users editeduser) throws CustomBusinessException {

		if (null != editeduser && null != fetchedUser && fetchedUser.getId() == editeduser.getId()) {

			// Mapping UserName and Password
			fetchedUser.setUsername((StringUtils.isNotBlank(editeduser.getUsername())) ? editeduser.getUsername()
					: fetchedUser.getUsername());
			fetchedUser.setPassword((StringUtils.isNotBlank(editeduser.getPassword())) ? editeduser.getPassword()
					: fetchedUser.getPassword());

			// Mapping AccountEnabledStatus and AccountLockedStatus
			fetchedUser.setAccountEnabledStatus(
					(null != editeduser.getAccountEnabledStatus() && editeduser.getAccountEnabledStatus() <= 1)
							? editeduser.getAccountEnabledStatus()
							: fetchedUser.getAccountEnabledStatus());
			fetchedUser.setAccountLockedStatus(
					(null != editeduser.getAccountLockedStatus() && editeduser.getAccountLockedStatus() <= 1)
							? editeduser.getAccountLockedStatus()
							: fetchedUser.getAccountLockedStatus());

			// Mapping CredentialsExpiryDate and AccountExpiryDate
			fetchedUser.setCredentialsExpiryDate(
					(null != editeduser.getCredentialsExpiryDate()) ? editeduser.getCredentialsExpiryDate()
							: fetchedUser.getCredentialsExpiryDate());
			fetchedUser.setAccountExpiryDate(
					(null != editeduser.getAccountExpiryDate()) ? editeduser.getAccountExpiryDate()
							: fetchedUser.getAccountExpiryDate());

			// Mapping roles

			// validating passed user roles with the existing roles in the database
			List<Integer> invalidRolesIds = validateUserRoles(editeduser);

			if (invalidRolesIds != null && invalidRolesIds.isEmpty()) {
				fetchedUser.setRoles(editeduser.getRoles());
			} else {

				// throwing Custom Exception for fail Scenarios
				throw new CustomBusinessException(
						"Invalid Roles Provided in Roles List  -- "
								+ invalidRolesIds.stream().map(Object::toString).collect(Collectors.joining(","))
								+ " --  Roles not found please add the roles and try again",
						HttpStatus.EXPECTATION_FAILED);
			}

		}

	}

	private boolean validateUser(Users user, boolean creation) throws CustomBusinessException {

		if (null != user) {
			if (StringUtils.isBlank(user.getUsername())) {
				throw new CustomBusinessException("Username missing !! Please Provide the Username and Try again",
						HttpStatus.NOT_ACCEPTABLE);
			}
			if (StringUtils.isBlank(user.getPassword())) {
				throw new CustomBusinessException("Password missing !! Please Provide the Password and Try again",
						HttpStatus.NOT_ACCEPTABLE);
			}

			Users fecthedUser;

			if (creation) {

				fecthedUser = customUserRepository.findByUsername(user.getUsername());

				if (null != fecthedUser) {

					throw new CustomBusinessException(
							" User with a different id already exists in the database , Already Existing Id for this role -- "
									+ fecthedUser.getId(),
							HttpStatus.NOT_ACCEPTABLE);
				}

			} else {

				fecthedUser = customUserRepository.findById(user.getId()).orElse(null);

				if (null == fecthedUser) {
					throw new CustomBusinessException(" User not found in the database , with id -- " + user.getId(),
							HttpStatus.NOT_ACCEPTABLE);
				}
			}

			// validating passed user roles with the existing roles in the database
			List<Integer> invalidRolesIds = validateUserRoles(user);

			if (invalidRolesIds != null && !invalidRolesIds.isEmpty()) {
				// throwing Custom Exception for fail Scenarios
				throw new CustomBusinessException("Invalid Roles Provided in Roles List  -- "
						+ invalidRolesIds.stream().map(Object::toString).collect(Collectors.joining(","))
						+ " --  Roles not found please add the roles and try again", HttpStatus.NOT_ACCEPTABLE);
			}

		}

		return true;

	}

	private List<Integer> validateUserRoles(Users editeduser) {

		List<Roles> roles = new ArrayList<>();
		if (null != editeduser && !CollectionUtils.isEmpty(editeduser.getRoles())) {

			// fetching all the roles in database by the role ids provided in the edited
			// user
			roles = roleRepository.findAllById(editeduser.getRoles().stream().map(Roles::getId).toList());
		}

		// getting only id out from Role object
		List<Integer> roleIds = roles.stream().map(Roles::getId).toList();

		// Comparing the fetched role ids with passed (editeduser) roleid and saving the
		// unmatched in a list
		return editeduser.getRoles().stream().map(Roles::getId).filter(role -> !roleIds.contains(role)).toList();

	}

	@Override
	public boolean deleteUser(Integer id) throws CustomBusinessException {

		Users userToBeDeleted = null;

		// User Availability Pre-Check
		userToBeDeleted = customUserRepository.findById(id).orElse(null);

		if (null == userToBeDeleted) {

			// throwing Custom Exception for fail Scenarios
			throw new CustomBusinessException("User with Provided Id - " + id + " -  is not found in the database.",
					HttpStatus.NOT_FOUND);
		}

		// Delete Operation
		customUserRepository.deleteById(id);

		// User Availability Post-Deletion-Check
		userToBeDeleted = customUserRepository.findById(id).orElse(null);

		if (null != userToBeDeleted) {

			// throwing Custom Exception for fail Scenarios
			throw new CustomBusinessException(
					"User with Provided Id - " + id + " -  cannot be deleted, possible reasons DB error, Access Issue",
					HttpStatus.EXPECTATION_FAILED);
		}
		return true;
	}

}
