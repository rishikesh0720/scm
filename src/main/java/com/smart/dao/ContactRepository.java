package com.smart.dao;

import java.util.List;

//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact,Integer>{
	
	// ContactRepository ke help se hum user ke sare contacts to fetch karenge but it also use dfor pagination
	
		// we should we thinking whats the need to create ContactRepository and shit 
		// as we can easily get user contacts 
		// through 3 steps easily  :-
		
		//  1) principal.getnName() --> userName 
		//  2) this.userRepository.getUserByUserName(userName) --> gives user from userName
		//  3) List<Contact> contacts =user.getContacts();
		
		// but from above way we can't get pagenation and by  
		// using ContactRepository we will implement pagenation property
		// this is one of the reason to go for pagenation 
	
	@Query("from Contact as c where c.user.id=:userId")
	public  Page<Contact> findContactsByUser(@Param("userId")int userId ,Pageable pePageable);
	// pageable is an Abstract interface for pagination information
//  param is Annotation to bind method 
    // parameters to a query via a named parameter.
   // pahle return type : public List<Contact> findContactsByUser(@Param("userId")int userId); isse login user ka contact milega
  // pagenation ko implement karne ke liye public :
 // Page<Contact> findContactsByUser(@Param("userId")int userId);
	
	// pageable ke pass 2 information hogi -> 1) current page = humne page variable use kiye hai as current page
  //                                         2) contact perpage 
	                                         // [yehan hum 5 use kar rahe par jitna mann utna contacts per page dikha sakte hain]
	
	
	
	

}
