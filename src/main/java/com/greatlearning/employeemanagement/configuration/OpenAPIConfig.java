package com.greatlearning.employeemanagement.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfig {

	@Bean
	public OpenAPI myOpenAPI() {

		Contact contact = new Contact();
		contact.setEmail("g1b1@mygreatlearning.com");
		contact.setName("G1B1");
		contact.setUrl("https://www.mygreatlearning.com");

		License glLicense = new License().name("GL License");

		Info info = new Info().title("Employee Management API").version("1.0").contact(contact)
				.description("This API exposes endpoints to Manage Employees.")
				.termsOfService("https://www.mygreatlearning.com/terms").license(glLicense);

		SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer Authentication");
		Components components = new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme());

		return new OpenAPI().addSecurityItem(securityRequirement).components(components).info(info);
	}

	private SecurityScheme createAPIKeyScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
	}

}