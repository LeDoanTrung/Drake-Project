package com.drake.project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	
	@Bean
	public UserDetailsService userDetailsService() {
		return new ShoppingUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public DaoAuthenticationProvider authenticationProvider () {
		DaoAuthenticationProvider authenProvider = new DaoAuthenticationProvider();
		authenProvider.setUserDetailsService(userDetailsService());
		authenProvider.setPasswordEncoder(passwordEncoder());
		
		return authenProvider;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Override  // cho trang html
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		.antMatchers("/users/**").hasAuthority("Admin") 
		.antMatchers("/categories/**").hasAnyAuthority("Admin","Editor")
		.antMatchers("/brands/**").hasAnyAuthority("Admin","Editor")
		.antMatchers("/products/**").hasAnyAuthority("Admin","Editor")
		//.anyRequest().permitAll()  - vào ko cần xác thực
		.anyRequest().authenticated() 
		.and()
		.formLogin()
			.loginPage("/login") // Đường dẫn Mapping đến MainController
			.usernameParameter("email") 
			.permitAll()
		.and().logout().permitAll()
		.and()
			.rememberMe()
				.key("Abc_123") 
				.tokenValiditySeconds(7 * 24 * 60 *60);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**","/js/**","/webjars/**");
	}
}
