package com.ofss.service;

import java.util.List;

import com.ofss.dto.CustomerDto;

public interface CustomerService {

	
	CustomerDto createCustomer(CustomerDto request);
	
	CustomerDto getCustomerById(Long customerId);
	
	List<CustomerDto> getAllCustomers();
	
	CustomerDto updateCustomer(CustomerDto request, Long customerId);
	
	CustomerDto patchCustomer(CustomerDto request, Long customerId);
	
	void deleteCustomer(Long customerId);
}
