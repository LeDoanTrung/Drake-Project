package com.drake.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;



@Entity
@Table(name = "customers")
public class Customer extends AbstractAddressWithCountry{

	@Column(nullable = false, unique = true, length = 45)
	private String email;
	
	@Column(nullable = false, length = 64)
	private String password;
	
	@Column(name = "verification_code", length = 64)
	private String verificationCode;
	
	private boolean enabled;
	
	@Column(name = "created_time")
	private Date createdTime;
	
	@Enumerated(EnumType.STRING)
	@Column()
	private AuthenticationType authenticationType;
	
	@Column(name = "reset_password_token", length = 64)
	private String resetPasswordToken;

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

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public AuthenticationType getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(AuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public Customer(String email, String password, String verificationCode, boolean enabled, Date createdTime,
			com.drake.common.entity.AuthenticationType authenticationType, String resetPasswordToken) {
		super();
		this.email = email;
		this.password = password;
		this.verificationCode = verificationCode;
		this.enabled = enabled;
		this.createdTime = createdTime;
		this.authenticationType = authenticationType;
		this.resetPasswordToken = resetPasswordToken;
	}

	public Customer() {
		super();
	}
	
	public Customer(Integer id) {
		this.id = id;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
}
