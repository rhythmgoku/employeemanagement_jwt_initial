package com.greatlearning.employeemanagement.dataloader;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.greatlearning.employeemanagement.entity.Roles;
import com.greatlearning.employeemanagement.entity.Users;
import com.greatlearning.employeemanagement.repository.RoleRepository;
import com.greatlearning.employeemanagement.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserPreDataLoader implements CommandLineRunner {

	@Value("${username.role.admin:#{admin}}")
	private String adminUsers;

	@Value("${username.role.user:#{user}}")
	private String normalUsers;

	@Autowired
	PasswordEncoder encoder;

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	public UserPreDataLoader(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public void run(String... args) throws Exception {

		log.info("populating intial user data");

		roleRepository.saveAll(
				List.of(Roles.builder().id(1).name("ADMIN").build(), Roles.builder().id(2).name("USER").build()));

		Arrays.asList(adminUsers.split(",")).stream()
				.forEach(enrty -> userRepository.save(Users.builder().username(enrty).password(encoder.encode(enrty))
						.accountEnabledStatus(1).accountExpiryDate(LocalDate.now().plusDays(5)).accountLockedStatus(1)
						.credentialsExpiryDate(LocalDate.now().plusDays(5))
						.roles(List.of(Roles.builder().id(1).name("ADMIN").build())).build()));

		Arrays.asList(normalUsers.split(",")).stream()
				.forEach(enrty -> userRepository.save(Users.builder().username(enrty).password(encoder.encode(enrty))
						.accountEnabledStatus(1).accountExpiryDate(LocalDate.now().plusDays(5)).accountLockedStatus(1)
						.credentialsExpiryDate(LocalDate.now().plusDays(5))
						.roles(List.of(Roles.builder().id(2).name("USER").build())).build()));

	}

}
