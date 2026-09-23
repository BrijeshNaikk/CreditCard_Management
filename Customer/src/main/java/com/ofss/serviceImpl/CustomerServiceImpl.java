package com.ofss.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ofss.dto.CustomerDto;
import com.ofss.entity.Customer;
import com.ofss.exceptions.BadRequestException;
import com.ofss.exceptions.ResourceNotFoundException;
import com.ofss.repository.CustomerRepository;
import com.ofss.service.CustomerService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public CustomerDto createCustomer(CustomerDto request) {
		Customer customer = new Customer();
		
		customer.setCustomerName(request.customerName());
		customer.setEmail(request.email());
		customer.setMobileNumber(request.mobileNumber());
		customer.setPanNumber(request.panNumber());
		
		Customer savedCustomer = customerRepository.save(customer);
		
		return toDto(savedCustomer);
	}

	@Override
	public CustomerDto getCustomerById(Long customerId) {
		Customer customer = findCustomerById(customerId);
		return toDto(customer);
	}

	@Override
	public List<CustomerDto> getAllCustomers() {
		return customerRepository.findAll()
				.stream()
				.map(this::toDto)
				.toList();
	}

	@Override
	public CustomerDto updateCustomer(CustomerDto request, Long customerId) {
		
		Customer customer = findCustomerById(customerId);
		
		customer.setCustomerName(request.customerName());
		customer.setEmail(request.email());
		customer.setMobileNumber(request.mobileNumber());
		customer.setPanNumber(request.panNumber());
		
		Customer updatedCustomer = customerRepository.save(customer);
		
		return toDto(updatedCustomer);
		
	}

	@Override
	public CustomerDto patchCustomer(CustomerDto request, Long customerId) {
		
		Customer customer= findCustomerById(customerId);
		
		if(request.customerName() != null) {
			customer.setCustomerName(request.customerName());
		}
		
		if(request.email() != null) {
			customer.setEmail(request.email());
		}
		
		if(request.mobileNumber() != null) {
			customer.setMobileNumber(request.mobileNumber());
		}
		
		if(request.panNumber() != null) {
			customer.setPanNumber(request.panNumber());
		}
		
		Customer updatedCustomer = customerRepository.save(customer);
		
		return toDto(updatedCustomer);
		
	}

	@Override
	public void deleteCustomer(Long customerId) {
		
		Customer customer = findCustomerById(customerId);
		
		customerRepository.delete(customer);
		
	}
	
	private Customer findCustomerById(Long customerId) {
		
		if(customerId == null || customerId <= 0) {
			throw new BadRequestException("Customer ID must be a positive number");
		}
		
		return customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", customerId.toString()));
	}
	
	
	private CustomerDto toDto(Customer customer) {
		return new CustomerDto(
				customer.getCustomerId(),
				customer.getCustomerName(),
				customer.getEmail(),
				customer.getMobileNumber(),
				customer.getPanNumber()
				);
	}
	

}
