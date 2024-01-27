package com.smart.entities;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

@Entity
@Table(name="CONTACT")  // table name ko change krna ka tarifa

public class Contact {
	
	// attributes of Contact table
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)//generate automatic id
	
	private int cId;
	
	
	private String name;
	
	
	private String secondName;
	
	
	private String work;
	
	
	private String email;
	
	
	private String phone;
	
	
	private String image;
	
	
	@Column(length=10000)
	private String description;
	
	//many contacts belong to one user so many to one relation
	@ManyToOne
	private User user;
	
	
	// getters and setters method 


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public int getcId() {
		return cId;
	}


	public void setcId(int cId) {
		this.cId = cId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSecondName() {
		return secondName;
	}


	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}


	public String getWork() {
		return work;
	}


	public void setWork(String work) {
		this.work = work;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object obj){
		
		return this.cId==((Contact)obj).getcId();
	}
	
//	@Override
//	public String toString() {
//		return "Contact [cId=" + cId + ", name=" + name + ", secondName=" + secondName + ", work=" + work + ", email="
//				+ email + ", phone=" + phone + ", image=" + image + ", description=" + description + ", user=" + user
//				+ "]";
//	}

	
	

}
