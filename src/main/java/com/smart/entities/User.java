package com.smart.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.OneToMany;
import javax.persistence.CascadeType;


@Entity 

@Table(name="USER")  //table name ko change krke USER set krdega         

public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)//isse id automatatic ek sequence me id generate hoga 
	
	private int id;
	
	@NotBlank(message=" Name Field Can Not Be Empty!! ")
	@Size(min=2,max=30,message=" Number of charachters not in range -> min=2 && max=30 ")
	
	private String name;
	
	
	 //@Email(message="Email should be valid ")
	@Email(message = "Email is not valid", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
	@NotEmpty(message = "Email cannot be empty")
	@Column(unique = true)
	private String email;
	
	  // @Size(min = 8,max=30, message = "Password should not be less than 8 charachters ")
	  // size ko max nahi kr sakte because hm password encode kar rahe password validator ke chalte me bahut dikkat ho raha tha error pe error aa raha tha 
	
	@NotBlank(message=" password Field Can Not Be Empty!! ")
	private String password;
	
	
	private String role;
	
	
	private boolean enabled;
	
	
	
	private String imageUrl;
	
	
	
	@Column(length=500)
	@Size(min = 10, max = 200, message 
    = "About Me must be between 10 and 200 characters")
  
	private String about;
	
	// one user can have many contacts -- relation is one to many here generate one more table 
	//but by using cascade it doesnot generate extra table and  mapped data with user table that present
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="user",orphanRemoval=true)
	//it's set to CascadeType.ALL, which means that all operations (PERSIST, MERGE, REMOVE, REFRESH, and DETACH) will be cascaded to the target entity.
	// orphanRemoval=true use krne se user ke sath uske sare  contacts bhi delete hojayega 
	//mappedBy="user"-->It establishes the bidirectional relationship between entities. In this case, the "user" field in the target entity is responsible for managing the relationship.
	
	private List<Contact> contacts=new ArrayList<>();
	
	//List<Integer> integerList = new ArrayList<Integer>();


  


	public List<Contact> getContacts() {
		return contacts;
	}


	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}


	// constructor
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}


  // getters and setter methods 
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getRole() {
		return role;
	}



	public void setRole(String role) {
		this.role = role;
	}



	public boolean isEnabled() {
		return enabled;
	}



	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	public String getImageUrl() {
		return imageUrl;
	}



	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	public String getAbout() {
		return about;
	}



	public void setAbout(String about) {
		this.about = about;
	}


	@Override
	
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", about=" + about + ", contacts=" + contacts
				+ "]";
	}
	
	
	
	

}
