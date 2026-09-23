package com.ofss.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ofss.dto.CustomerDto;
import com.ofss.entity.Customer;
import com.ofss.repository.CustomerRepository;
import com.ofss.service.CustomerService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerRepository customerRespository;
	
	@Override
	public CustomerDto createCustomer(CustomerDto request) {
		Customer customer = new Customer(
				request.customerName(),
				request.email(),
				request.mobileNumber(),
				request.panNumber()
				);
		
		Customer savedCustomer = customerRespository.save(customer);
		
		return toDto(savedCustomer);
	}

	@Override
	public CustomerDto getCustomerById(Long customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CustomerDto> getAllCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerDto updateCustomer(CustomerDto request, Long customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerDto patchCustomer(CustomerDto request, Long customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCustomer(Long customerId) {
		// TODO Auto-generated method stub
		
	}
	

}
