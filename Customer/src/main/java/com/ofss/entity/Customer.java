package com.ofss.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(
	        name = "customer_seq",
	        sequenceName = "CUSTOMER_SEQ",
	        allocationSize = 1
	    )
	@Column(name = "customer_id")
	private Long customerId;
	
	
	@Column(name = "customer_name", nullable = false, length = 100)
	private String customerName;
	
	@Column(name = "email", nullable = false, unique = true, length = 150)
	private String email;
	
	@Column(name = "mobile_number", nullable = false, length = 10)
	private String mobileNumber;
	
	@Column(name = "pan_number", nullable = false, unique = true, length = 10)
	private String panNumber;

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Customer(Long customerId, String customerName, String email, String mobileNumber, String panNumber) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.panNumber = panNumber;
	}
	
	public Customer(String customerName, String email, String mobileNumber, String panNumber) {
		this.customerName = customerName;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.panNumber = panNumber;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	
	

}
