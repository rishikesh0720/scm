package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

/**
 * //UserDetailsService is an interface having some inbuilt meth0ds is class me
 * database se user ko layenge or return kr denge for authentication it uses the
 * previous userdeatils as method public userdetais.....
 */

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;// to call getUserByUserName method to get email as username

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// method to fetch user from the database
		User user = userRepository.getUserByUserName(username);// yhe se hme username milega
		if (user == null) {
			throw new UsernameNotFoundException("could not found user ");
		}
		// agar null nhi hai to hm user details return kardnege
		CustomUserDetail customUserDetail = new CustomUserDetail(user);
		// yha hm user ko customuserdetail class me passs
		// kr denge or phir ise return kr denge
		// ise custom user details class me jo method hai unke pass is user ke hisab se
		// value assign ho jayega phir use kr lenge

		return customUserDetail;
	}

}
