package com.ecommerce.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class ConfigSecurity extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/**")
			.authorizeRequests()
				.antMatchers("/", "/login**").permitAll()
			.anyRequest()
				.authenticated()
			.and()
				.logout()
				.logoutSuccessUrl("/").permitAll();
	}
}
