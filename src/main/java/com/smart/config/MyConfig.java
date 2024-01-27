package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@configuration used to declare one or more bean
@Configuration
@EnableWebSecurity

public class MyConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService getUserDetailService() {

		return new UserDetailsServiceImpl();// UserDetailsServiceImpl isse return krne se custom user details bhi call
											// ho jayega kyo ki
		// isme customuserdetails ka object bnaye hai sath me vaiuse bhi user ka fetch
		// ho jayega see copy for more details
	}
//decrypt password means change given password into other format for security reason
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
// This class is provided by Spring Security and is used to perform authentication based on information retrieved from a UserDetailsService.
		
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
		daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
         // sets the UserDetailsService for the authentication provider
		//. getUserDetailService() is presumably a method that returns an implementation of the UserDetailsService interface
		
		return daoAuthenticationProvider;

	}

	// **** configure method
//this method tells that which type of authentication you provide here we use database(dao)authentication
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
//yha kis role ko kaun sa url acces dena hai user ke login ke bad kaun kaun sa url kam krega without login not work
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasRole("USER")
				.antMatchers("/**").permitAll().and().formLogin().loginPage("/signin").loginProcessingUrl("/dologin")
				.defaultSuccessUrl("/user/index").and().csrf().disable();

	} 
	//.failureUrl("/login-fail")--> agar custom error page hoga tb
	// so that our sign in page is shown not by default spring sign in
//Disabling CSRF (Cross-Site Request Forgery) protection in Spring Security is generally not recommended
	//To disable CSRF protection in Spring Security, you can configure it in your Spring Security configuration class
//http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasRole("USER") means
	//jitne bhi url /user se ho or role user ho usko authorized kr dega
	//.antMatchers("/**").permitAll()-->baki user or admin url ko chhodkar permit de denge 
//.formLogin().loginPage("/signin")-->means login page sigin page hai
	//.loginProcessingUrl("/dologin")-->means username and oassword jo url pass kiye hai tha stpred yha kuchh bhi url pass kr sakte
	//wo default hai or same form action me pass kr denge url dologin
	//.defaultSuccessUrl("/user/index")-->login succes hone ke bad kaun sa url run hoga yha "/user/index" run hoga


}
