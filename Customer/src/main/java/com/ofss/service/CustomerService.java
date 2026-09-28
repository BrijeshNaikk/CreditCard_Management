package com.ofss.service;

import java.util.List;

import com.ofss.dto.CustomerDto;
import com.ofss.entity.Customer;

public interface CustomerService {

	
	CustomerDto createCustomer(
	        CustomerDto request,
	        String authorizationHeader
	);
	
	CustomerDto getCustomerById(Long customerId);
	
	List<CustomerDto> getAllCustomers();
	
	CustomerDto updateCustomer(CustomerDto request, Long customerId);
	
	CustomerDto patchCustomer(CustomerDto request, Long customerId);
	
	void deleteCustomer(Long customerId);
	

	CustomerDto getCustomerForLoggedInUser(Long userId);
}
