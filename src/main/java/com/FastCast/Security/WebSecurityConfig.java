package com.FastCast.Security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.FastCast.Security.JWT.JwtRequestFilter;

import com.FastCast.Services.Userservice;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public UserDetailsService userDetailsService() {
		return new Userservice();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
				.csrf().disable().cors()
				.and()
				.authorizeRequests()
				.antMatchers("/signin").permitAll()
				.antMatchers("/signup/**").permitAll()
				.antMatchers("/api/video/get/public/all").permitAll()
				.antMatchers("/api/video/stream").permitAll()
				.antMatchers("/api/video/thumb").permitAll()
				.antMatchers("/api/").permitAll()
				.anyRequest().authenticated()
				.and()

				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// .logout().logoutRequestMatcher(new
		// AntPathRequestMatcher("/logout")).logoutSuccessUrl("/api/")
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	}

}
