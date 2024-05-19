package com.greatlearning.employeemanagement.entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
@JsonIgnoreProperties(allowSetters = true, value = { "accountNonLocked", "enabled", "authorities", "accountNonExpired",
		"credentialsNonExpired" })
@Schema(description = "User Info")
public class Users implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	@Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "1")
	private Integer id;

	@Schema(example = "admin")
	private String username;

	@Schema(example = "p**w**d")
	private String password;

	@Schema(example = "2024-05-19T19:47:42.000+00:00")
	@JsonIgnore
	private LocalDate accountExpiryDate;

	@Schema(example = "1")
	@JsonIgnore
	private Integer accountLockedStatus;

	@Schema(example = "2024-05-19T19:47:42.000+00:00")
	@JsonIgnore
	private LocalDate credentialsExpiryDate;

	@Schema(example = "1")
	@JsonIgnore
	private Integer accountEnabledStatus;

	@Schema(example = "[{\"id\":\"1\",\"name\":\"SUPERUSER\"}]")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "users_id"), inverseJoinColumns = @JoinColumn(name = "roles_id"))
	private List<Roles> roles;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.getAccountExpiryDate().isAfter(LocalDate.now());
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.getAccountLockedStatus() == 1;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.getCredentialsExpiryDate().isAfter(LocalDate.now());
	}

	@Override
	public boolean isEnabled() {
		return this.getAccountEnabledStatus() == 1;
	}

}