package com.ofss.controller;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ofss.validation.Patch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ofss.dto.CustomerDto;
import com.ofss.entity.Customer;
import com.ofss.security.JwtUserPrincipal;
import com.ofss.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;

	
	 // POST /api/customers
	@PostMapping
	public ResponseEntity<CustomerDto> createCustomer(
	        @Valid @RequestBody CustomerDto request,
	        @RequestHeader(HttpHeaders.AUTHORIZATION)
	        String authorizationHeader
	) {

	    CustomerDto createdCustomer =
	            customerService.createCustomer(
	                    request,
	                    authorizationHeader
	            );

	    return ResponseEntity
	            .status(HttpStatus.CREATED)
	            .body(createdCustomer);
	}

	@GetMapping("/{customerId}")
	public ResponseEntity<CustomerDto> getCustomerById(
	        @PathVariable Long customerId
	) {

	    CustomerDto customer = customerService.getCustomerById(customerId);

	    return ResponseEntity.ok(customer);
	}
    
//    @GetMapping("/{customerId}")
//    public Customer getCustomerById(
//            @PathVariable Long customerId,
//            @AuthenticationPrincipal JwtUserPrincipal loggedInUser) {
//
//        Long loggedInUserId = loggedInUser.userId();
//
//        return customerService.getCustomerForLoggedInUser(
//                customerId,
//                loggedInUserId
//        );
//    }

    // GET /api/customers
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {

        List<CustomerDto> customers = customerService.getAllCustomers();

        return ResponseEntity.ok(customers);
    }
    
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDto> updateCustomer(@Valid @RequestBody CustomerDto request, @PathVariable("customerId") Long customerId){
    	
    	CustomerDto customer = customerService.updateCustomer(request, customerId);
    	
    	return ResponseEntity.ok(customer);
    	
    }
    
    @PatchMapping("/{customerId}")
    public ResponseEntity<CustomerDto> patchCustomer(@Validated(Patch.class) @RequestBody CustomerDto request, @PathVariable("customerId") Long customerId){
    	
    	CustomerDto customer = customerService.patchCustomer(request, customerId);
    	
    	return ResponseEntity.ok(customer);
    	
    }
    
    
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") Long customerId){
    	
    	customerService.deleteCustomer(customerId);
    	
    	return ResponseEntity.noContent().build();
    	
    }
    
    @GetMapping("/me")
    public ResponseEntity<CustomerDto> getMyCustomer(
            @AuthenticationPrincipal JwtUserPrincipal loggedInUser
    ) {

        CustomerDto customer = customerService.getCustomerForLoggedInUser(
                loggedInUser.userId()
        );

        return ResponseEntity.ok(customer);
    }
}
