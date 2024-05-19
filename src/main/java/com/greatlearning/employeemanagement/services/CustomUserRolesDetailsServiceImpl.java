package com.greatlearning.employeemanagement.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.greatlearning.employeemanagement.entity.Roles;
import com.greatlearning.employeemanagement.exception.CustomBusinessException;
import com.greatlearning.employeemanagement.repository.RoleRepository;

@Service
public class CustomUserRolesDetailsServiceImpl implements CustomUserRolesDetailsService {

	@Autowired
	RoleRepository roleRepository;

	@Override
	public Roles saveUserRole(Roles role) throws CustomBusinessException {

		// validating role information
		validateRole(role);

		// Saving the New Role
		Roles savedRole = null;

		try {
			savedRole = roleRepository.save(role);
		} catch (Exception e) {
			throw new CustomBusinessException("Unable to Save the Role with Role Name" + role.getName(),
					HttpStatus.EXPECTATION_FAILED);
		}

		return savedRole;
	}

	private void validateRole(Roles role) throws CustomBusinessException {

		Roles fecthedRole = null;

		role.setName(role.getName().toUpperCase());

		if (null != role) {

			if (StringUtils.isNotBlank(role.getName())) {
				fecthedRole = roleRepository.findByName(role.getName());
			} else {
				throw new CustomBusinessException("Role Name missing !! Please Provide the Role Name and Try again",
						HttpStatus.NOT_ACCEPTABLE);
			}
		}

		if (null != fecthedRole) {
			throw new CustomBusinessException(
					" Role with a different id already exists in the database , Already Existing Id for this role -- "
							+ fecthedRole.getId(),
					HttpStatus.NOT_ACCEPTABLE);
		}

	}

	@Override
	public List<Roles> getAllUserRoles() throws CustomBusinessException {

		List<Roles> roles = new ArrayList<Roles>();

		try {
			roles = roleRepository.findAll();
		} catch (Exception e) {
			throw new CustomBusinessException("Unable to fecth All Roles from database", HttpStatus.EXPECTATION_FAILED);
		}

		return roles;
	}

	@Override
	public Roles findUserRoleById(Integer id) throws CustomBusinessException {

		Roles role = null;

		if (null != id && id > 0) {
			role = roleRepository.findById(id).get();
		} else {
			throw new CustomBusinessException("Invalid Role Id Prvided", HttpStatus.NOT_ACCEPTABLE);
		}

		if (null == role) {
			throw new CustomBusinessException("Role with Provided Id - " + id + " -  is not found in the database.",
					HttpStatus.NOT_FOUND);
		}
		return role;
	}

	@Override
	public Roles editUserRole(Roles editedRole) throws CustomBusinessException {

		Roles fecthedRole = null;

		if (null != editedRole && 0 != editedRole.getId()) {

			// Role Availability Pre-Check
			fecthedRole = roleRepository.findById(editedRole.getId()).get();
		}

		// Role Mapper for Coping the editedRole data to fecthedRole data
		roleMapper(fecthedRole, editedRole);

		Roles savedRole = roleRepository.save(fecthedRole);

		return savedRole;
	}

	private void roleMapper(Roles fecthedRole, Roles editedRole) {

		if (null != editedRole && null != fecthedRole && fecthedRole.getId() == editedRole.getId()
				&& StringUtils.isNotBlank(editedRole.getName())) {

			fecthedRole.setName(editedRole.getName());

		}
	}

	@Override
	public boolean deleteUserRole(Integer id) throws CustomBusinessException {

		Roles userRoleToBeDeleted = null;

		// Role Availability Pre-Check
		userRoleToBeDeleted = roleRepository.findById(id).orElse(null);

		if (null == userRoleToBeDeleted) {

			// throwing Custom Exception for fail Scenarios
			throw new CustomBusinessException("Role with Provided Id - " + id + " -  is not found in the database.",
					HttpStatus.NOT_FOUND);
		}

		// Delete Operation
		roleRepository.deleteById(id);

		// Role Availability Post-Deletion-Check
		userRoleToBeDeleted = roleRepository.findById(id).orElse(null);

		if (null != userRoleToBeDeleted) {

			// throwing Custom Exception for fail Scenarios
			throw new CustomBusinessException(
					"Role with Provided Id - " + id + " -  cannot be deleted, possible reasons DB error, Access Issue",
					HttpStatus.EXPECTATION_FAILED);
		}

		return false;
	}

}
