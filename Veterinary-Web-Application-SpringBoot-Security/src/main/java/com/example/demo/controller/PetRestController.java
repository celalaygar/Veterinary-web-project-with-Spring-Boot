package com.example.demo.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Customer;
import com.example.demo.model.Pet;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PetRepository;

@RestController
@RequestMapping("/rest")
public class PetRestController {
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	PetRepository petRepository;
	
	
	@RequestMapping(value = "/pets/{customerid}", method = RequestMethod.GET)
	public ResponseEntity<List<Pet>> findAllPets(@PathVariable int customerid, Map<String, Object> map) throws SQLException {

		Customer customer = customerRepository.findById(customerid).get();
		List<Pet> pets = petRepository.findByCustomer(customer);
		return  ResponseEntity.ok(pets);

		
	}
	
}
