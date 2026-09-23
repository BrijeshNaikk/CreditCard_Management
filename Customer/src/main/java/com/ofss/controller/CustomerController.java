package com.ofss.controller;

import java.util.List;

import com.ofss.validation.Patch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @Valid @RequestBody CustomerDto request
    ) {

        CustomerDto customer = customerService.createCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customer);
    }

    // GET /api/customers/{customerId}
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(
            @PathVariable("customerId") Long customerId
    ) {

        CustomerDto customer = customerService.getCustomerById(customerId);

        return ResponseEntity.ok(customer);
    }

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
}
