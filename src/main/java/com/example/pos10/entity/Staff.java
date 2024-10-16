package com.example.pos10.entity;

import java.time.LocalDateTime;

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

	// 用來記錄錯誤次數
	@Column(name = "error_count")
	private Integer errorCount = 0;

	// 用來記錄封鎖的時間
	@Column(name = "block_time")
	private LocalDateTime blockTime;

	@Column(name = "first_login")
	private Boolean firstLogin = true;

	public Staff() {
		super();
	}

	public Staff(String staffNumber, @NotNull String pwd, @NotNull String name, @NotNull String phone,
			@NotNull String authorization, @Email String email, Integer errorCount, LocalDateTime blockTime,
			Boolean firstLogin) {
		super();
		this.staffNumber = staffNumber;
		this.pwd = pwd;
		this.name = name;
		this.phone = phone;
		this.authorization = authorization;
		this.email = email;
		this.errorCount = errorCount;
		this.blockTime = blockTime;
		this.firstLogin = firstLogin;
	}

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

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public LocalDateTime getBlockTime() {
		return blockTime;
	}

	public void setBlockTime(LocalDateTime blockTime) {
		this.blockTime = blockTime;
	}

	public Boolean getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

}
