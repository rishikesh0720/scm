package com.smart.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;

import com.smart.entities.User;


//UserDetails is an interface here some
// inbuilt method present that uses as purpose of spring security
public class CustomUserDetail implements UserDetails{
	
	private User user;// by using user we can get all details about user like  role email etc
	 

	public CustomUserDetail(User user) {
		super();
		this.user = user;
	}
	//here describe the authority of user yha user ka role hi to authoriyu hai
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(user.getRole());
		return List.of(simpleGrantedAuthority);//return list of all role yype like admin or user
	}

	@Override
	public String getPassword() {
		
		return user.getPassword();
		
	}

	@Override
	public String getUsername() {
		
		return user.getEmail();// kyun ki signup ke liye hmne username as email rakha hai 
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}

}
