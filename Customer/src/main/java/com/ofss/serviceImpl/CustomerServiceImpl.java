package com.ofss.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ofss.client.SecurityServiceClient;
import com.ofss.client.SecurityUserResponse;
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
	
	private final SecurityServiceClient securityServiceClient;
	
	public CustomerServiceImpl(
	        CustomerRepository customerRepository,
	        SecurityServiceClient securityServiceClient
	) {
	    this.customerRepository = customerRepository;
	    this.securityServiceClient = securityServiceClient;
	}
	
	@Override
	public CustomerDto createCustomer(
	        CustomerDto request,
	        String authorizationHeader
	) {

	    SecurityUserResponse securityUser =
	            securityServiceClient.getUserById(
	                    request.userId(),
	                    authorizationHeader
	            );

	    if (!securityUser.enabled()) {
	        throw new BadRequestException(
	                "User with ID " + request.userId()
	                        + " is disabled"
	        );
	    }

	    if (!"USER".equals(securityUser.role())) {
	        throw new BadRequestException(
	                "A customer profile can be created only for a USER role account"
	        );
	    }

	    if (customerRepository.existsById(request.userId())) {
	        throw new BadRequestException(
	                "A customer profile already exists for user ID "
	                        + request.userId()
	        );
	    }

	    Customer customer = new Customer();

	    customer.setCustomerId(request.userId());
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
		customer.setCustomerId(request.userId());
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
				customer.getCustomerId(),
				customer.getCustomerName(),
				customer.getEmail(),
				customer.getMobileNumber(),
				customer.getPanNumber()
				);
	}

	
	
	@Override
	@Transactional
	public CustomerDto getCustomerForLoggedInUser(Long userId) {

	    Customer customer = customerRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Customer",
	                    userId.toString()
	            ));

	    return toDto(customer);
	}
	
	
	

}
