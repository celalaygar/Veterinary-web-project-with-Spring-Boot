package com.example.demo.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;


@RestController
@RequestMapping("/rest")
public class CustomerRestController {
	@Autowired
	CustomerRepository customerRepository;
	
	// Get All Customers
	@GetMapping("/customers")
	public List<Customer> HelloPage() {
		List<Customer> customers = customerRepository.findAll();
		return customers;
	}
	
	//http://localhost:8182/rest/customer/5
	//http://localhost:8182/rest/customer/8
	@RequestMapping(value = "/customer/{customerid}", method = RequestMethod.GET)
	public Customer CustomerShowPanel(@PathVariable int customerid, Map<String, Object> map) throws SQLException {
		//Customer customer = customerRepository.findById(customerid).get();
		
		return customerRepository.findById(customerid).orElseThrow(()-> new ResourceNotFoundException("Customer", "id", customerid));
	}

	
//	@RequestMapping(value = "/customer/{customerid}", method = RequestMethod.GET)
//	public ResponseEntity<Customer> CustomerShowPanel(@PathVariable int customerid, Map<String, Object> map) throws SQLException {
//		Customer customer = customerRepository.findById(customerid).get();
//		
//		return  ResponseEntity.ok(customer);
//	}

	
}
