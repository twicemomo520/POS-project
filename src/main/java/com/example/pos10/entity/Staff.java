package com.example.pos10.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "staff")
public class Staff {

	@Id
	@Column(name = "staff_number")
	private String staffNumber;

	@NotNull
	@Column(name = "pwd", length = 45, nullable = false)
	private String pwd;

	@NotNull
	@Column(name = "name", length = 45, nullable = false)
	private String name;

	@NotNull
	@Column(name = "phone", length = 45, nullable = false)
	private String phone;

	@NotNull
	@Column(name = "authorization", length = 45, nullable = false)
	private String authorization;

	@Email
	@Column(name = "email")
	private String email;

	
	public Staff() {
		super();
	}


	public Staff(String staffNumber, String pwd, String name, String phone, String authorization, String email) {
		this.staffNumber = staffNumber;
		this.pwd = pwd;
		this.name = name;
		this.phone = phone;
		this.authorization = authorization;
		this.email = email; 
	}

	// Getters and Setters
	public String getStaffNumber() {
		return staffNumber;
	}

	public void setStaffNumber(String staffNumber) {
		this.staffNumber = staffNumber;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public String getEmail() { 
		return email;
	}

	public void setEmail(String email) { 
		this.email = email;
	}
}
